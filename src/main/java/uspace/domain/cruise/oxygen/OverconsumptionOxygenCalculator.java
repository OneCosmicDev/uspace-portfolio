package uspace.domain.cruise.oxygen;

import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.crew.Crew;

public class OverconsumptionOxygenCalculator implements OxygenCalculator {
    private final double remainingTimeInMinutes;
    private final double securityMarginMultiplier;
    private final double overconsumptionTravelerMultiplier;

    public OverconsumptionOxygenCalculator(double remainingTimeInMinutes, double securityMarginMultiplier, double overconsumptionTravelerMultiplier) {
        this.remainingTimeInMinutes = remainingTimeInMinutes;
        this.securityMarginMultiplier = securityMarginMultiplier;
        this.overconsumptionTravelerMultiplier = overconsumptionTravelerMultiplier;
    }


    @Override
    public Oxygen calculateRequiredOxygen(CruiseBookings cruiseBookings, Crew crew) {
        Oxygen travelersOxygenConsumptionPerMinutes = cruiseBookings.getTotalOxygenConsumptionPerMinutes().multiply(overconsumptionTravelerMultiplier);
        Oxygen crewOxygenConsumptionPerMinutes = crew.getTotalOxygenConsumptionPerMinutes();

        return travelersOxygenConsumptionPerMinutes.add(crewOxygenConsumptionPerMinutes)
                                                   .multiply(remainingTimeInMinutes)
                                                    .multiply(securityMarginMultiplier);
    }
}
