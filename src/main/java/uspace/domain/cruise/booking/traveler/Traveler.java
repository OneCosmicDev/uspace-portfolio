package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.SportName;
import uspace.domain.cruise.sport.exceptions.SportAlreadyBookedException;
import uspace.domain.cruise.sport.exceptions.SportNotBookableException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Traveler {

    @Id
    protected TravelerId id;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    protected List<Badge> badges;

    private TravelerName name;

    @Enumerated(EnumType.STRING)
    private TravelerCategory category;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<SportName> sports;

    private Oxygen oxygenConsumptionPerMinute;

    protected Traveler() {

    }

    protected Traveler(TravelerId id, TravelerName name, TravelerCategory category, List<Badge> badges, Oxygen oxygenConsumptionPerMinute, List<SportName> sports)
    {
        this.id = id;
        this.name = name;
        this.category = category;
        this.badges = badges;
        this.oxygenConsumptionPerMinute = oxygenConsumptionPerMinute;
        this.sports = sports;
    }

    public TravelerId getId() {
        return id;
    }

    public TravelerName getName() {
        return name;
    }

    public TravelerCategory getCategory() {
        return category;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public abstract void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience,
                                                   List<Traveler> travelersInBooking);

    public abstract boolean canAccompagnyChildForZeroGravityExperience(ZeroGravityExperience zeroGravityExperience);

    public Oxygen getOxygenConsumptionPerMinute() {
        return oxygenConsumptionPerMinute;
    }

    public void bookSport(Sport sport) {
        if (isSportBooked(sport))
        {
            throw new SportAlreadyBookedException();
        }

        if (!sport.canBeBookedBy(category))
        {
            throw new SportNotBookableException();
        }

        sports.add(sport.getName());
        earnSportBadge();
    }

    protected void earnOneTimeBadge(Badge badge) {
        if (!badges.contains(badge))
        {
            badges.add(badge);
        }
    }

    protected abstract void earnSportBadge();

    private boolean isSportBooked(Sport sport) {
        return sports.stream().anyMatch(s -> s.equals(sport.getName()));
    }
}
