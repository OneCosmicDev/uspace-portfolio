package uspace.domain.cruise.oxygen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OxygenCalculatorFactoryTest {

    private static final double ANY_OVERCONSUMPTION_CALCULATION_MINIMUM_TIME_RATIO = 0.2;
    private static final double ANY_SECURITY_MARGIN_MULTIPLIER = 1.15;
    private static final double ANY_OVERCONSUMPTION_TRAVELER_MULTIPLIER = 1.08;
    private static final CruiseDateTime ANY_CRUISE_DEPARTURE_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2888,1,1,0,0));
    private static final CruiseDateTime ANY_CRUISE_END_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2888,2,1,0,0));
    private OxygenCalculatorFactory oxygenCalculatorFactory;

    @BeforeEach
    void createOxygenCalculatorFactory(){
        oxygenCalculatorFactory = new OxygenCalculatorFactory(ANY_OVERCONSUMPTION_CALCULATION_MINIMUM_TIME_RATIO, ANY_SECURITY_MARGIN_MULTIPLIER, ANY_OVERCONSUMPTION_TRAVELER_MULTIPLIER);
    }

    @Test
    void givenRemainingTimePercentageMoreOrEqualToOverconsumptionCalculationMinimumTimeRatio_whenCreate_thenReturnOverconsumptionOxygenCalculator() {
        CruiseDateTime calculationDateTimeWithRemainingTimePercentageMoreOrEqualToOverconsumptionCalculationMinimumTimeRatio = new CruiseDateTime(LocalDateTime.of(2888,1,1,1,0));

        OxygenCalculator oxygenCalculator = oxygenCalculatorFactory.create(calculationDateTimeWithRemainingTimePercentageMoreOrEqualToOverconsumptionCalculationMinimumTimeRatio,
                                                                           ANY_CRUISE_DEPARTURE_DATE_TIME, ANY_CRUISE_END_DATE_TIME);

        assertTrue(oxygenCalculator instanceof OverconsumptionOxygenCalculator);
    }

    @Test
    void givenRemainingTimePercentageLessThanOverconsumptionCalculationMinimumTimeRatio_whenCreate_thenReturnOverconsumptionOxygenCalculator() {
        CruiseDateTime calculationDateTimeWithRemainingTimePercentageLessThanOverconsumptionCalculationMinimumTimeRatio = new CruiseDateTime(LocalDateTime.of(2888,1,30,0,0));

        OxygenCalculator oxygenCalculator = oxygenCalculatorFactory.create(calculationDateTimeWithRemainingTimePercentageLessThanOverconsumptionCalculationMinimumTimeRatio,
                                                                           ANY_CRUISE_DEPARTURE_DATE_TIME, ANY_CRUISE_END_DATE_TIME);

        assertTrue(oxygenCalculator instanceof StandardOxygenCalculator);
    }

}