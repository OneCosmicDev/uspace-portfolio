package uspace.domain.cruise.oxygen;

import jakarta.persistence.Embeddable;
import uspace.domain.cruise.dateTime.CruiseDateTime;

@Embeddable
public class OxygenCalculatorFactory {
    private double overconsumptionCalculationMinimumTimeRatio;
    private double securityMarginMultiplier;
    private double overconsumptionTravelerMultiplier;

    public OxygenCalculatorFactory() {}

    public OxygenCalculatorFactory(double overconsumptionCalculationMinimumTimeRatio, double securityMarginMultiplier,
                                   double overconsumptionTravelerMultiplier) {
        this.overconsumptionCalculationMinimumTimeRatio = overconsumptionCalculationMinimumTimeRatio;
        this.securityMarginMultiplier = securityMarginMultiplier;
        this.overconsumptionTravelerMultiplier = overconsumptionTravelerMultiplier;
    }

    public OxygenCalculator create(CruiseDateTime calculationCruiseDateTime, CruiseDateTime cruiseDepartureDateTime, CruiseDateTime cruiseEndDateTime) {

        double cruiseTotalDurationInMinutes = cruiseDepartureDateTime.calculateMinutesBefore(cruiseEndDateTime);
        double remainingTimeInMinutes = calculationCruiseDateTime.calculateMinutesBefore(cruiseEndDateTime);
        double remainingTimePercentage = remainingTimeInMinutes/cruiseTotalDurationInMinutes;

        if (remainingTimePercentage >= overconsumptionCalculationMinimumTimeRatio) {
            return new OverconsumptionOxygenCalculator(remainingTimeInMinutes, securityMarginMultiplier, overconsumptionTravelerMultiplier);
        } else {
            return new StandardOxygenCalculator(remainingTimeInMinutes);
        }
    }
}
