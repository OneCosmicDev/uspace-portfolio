package uspace.domain.cruise.oxygen;

import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.crew.Crew;

public class StandardOxygenCalculator implements OxygenCalculator {
    private final double remainingTimeInMinutes;
    public StandardOxygenCalculator(double remainingTimeInMinutes) {
        this.remainingTimeInMinutes = remainingTimeInMinutes;
    }

    @Override
    public Oxygen calculateRequiredOxygen(CruiseBookings cruiseBookings, Crew crew) {
        Oxygen travelersOxygenConsumptionPerMinutes = cruiseBookings.getTotalOxygenConsumptionPerMinutes();
        Oxygen crewOxygenConsumptionPerMinutes = crew.getTotalOxygenConsumptionPerMinutes();

        return travelersOxygenConsumptionPerMinutes.add(crewOxygenConsumptionPerMinutes).multiply(remainingTimeInMinutes);
    }
}
