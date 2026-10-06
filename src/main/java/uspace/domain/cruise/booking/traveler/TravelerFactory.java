package uspace.domain.cruise.booking.traveler;

import jakarta.inject.Inject;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.InvalidTravelerCategoryException;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.SportName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelerFactory {

    private final Map<TravelerCategory, Oxygen> travelersOxygenConsumptionPerMinute;

    @Inject
    public TravelerFactory(Map<TravelerCategory, Oxygen> travelersOxygenConsumptionPerMinute) {
        this.travelersOxygenConsumptionPerMinute = travelersOxygenConsumptionPerMinute;
    }

    public Traveler create(String travelerNameStr, TravelerCategory travelerCategory) {
        TravelerId travelerId = new TravelerId();
        TravelerName travelerName = new TravelerName(travelerNameStr);
        List<Badge> noBadgesInitial = new ArrayList<>();
        Oxygen oxygenConsumptionPerMinute = travelersOxygenConsumptionPerMinute.get(travelerCategory);
        List<SportName> noSportInitial = new ArrayList<>();

        return switch (travelerCategory) {
            case ADULT -> new AdultTraveler(travelerId, travelerName, noBadgesInitial, oxygenConsumptionPerMinute, noSportInitial);
            case CHILD -> new ChildTraveler(travelerId, travelerName, noBadgesInitial, oxygenConsumptionPerMinute, noSportInitial);
            case SENIOR -> new SeniorTraveler(travelerId, travelerName, noBadgesInitial, oxygenConsumptionPerMinute, noSportInitial);
            default -> throw new InvalidTravelerCategoryException();
        };
    }
}
