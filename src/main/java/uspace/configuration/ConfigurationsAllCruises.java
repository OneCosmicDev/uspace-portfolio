package uspace.configuration;

import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.oxygen.Oxygen;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationsAllCruises {
    public static final Oxygen CREW_MEMBER_OXYGEN_CONSUMPTION_PER_MINUTE = new Oxygen(250);
    public static final Map<TravelerCategory, Oxygen> TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTE =
            new HashMap<>(Map.of(
                TravelerCategory.ADULT, new Oxygen(250),
                TravelerCategory.CHILD, new Oxygen(150),
                TravelerCategory.SENIOR, new Oxygen(300)));
}
