package uspace.domain.cruise.oxygen;

import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.crew.Crew;

public interface OxygenCalculator {
    Oxygen calculateRequiredOxygen(CruiseBookings cruiseBookings, Crew crew);
}
