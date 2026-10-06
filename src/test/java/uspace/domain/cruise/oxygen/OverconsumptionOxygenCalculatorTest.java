package uspace.domain.cruise.oxygen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.crew.Crew;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverconsumptionOxygenCalculatorTest {

    private static final double ANY_SECURITY_MARGIN_MULTIPLIER = 1.5;
    private static final double ANY_OVERCONSUMPTION_TRAVELER_MULTIPLIER = 1.1;
    private static final double ANY_REMAINING_TIME_IN_MINUTES = 2;
    private static final Oxygen ANY_TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTES = new Oxygen(500);
    private static final Oxygen ANY_CREW_OXYGEN_CONSUMPTION_PER_MINUTES = new Oxygen(1400);
    private OverconsumptionOxygenCalculator overconsumptionOxygenCalculator;
    @Mock
    private CruiseBookings cruiseBookingsMock;
    @Mock
    private Crew crewMock;

    @BeforeEach
    void createOverconsumptionOxygenCalculator() {
        overconsumptionOxygenCalculator = new OverconsumptionOxygenCalculator(ANY_REMAINING_TIME_IN_MINUTES, ANY_SECURITY_MARGIN_MULTIPLIER, ANY_OVERCONSUMPTION_TRAVELER_MULTIPLIER);
    }

    @Test
    void whenCalculateRequiredOxygen_thenReturnCrewAndTravelersConsumptionForRemainingTimeApplyingSecurityMarginAndTravelerMultiplier() {
        when(cruiseBookingsMock.getTotalOxygenConsumptionPerMinutes()).thenReturn(ANY_TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTES);
        when(crewMock.getTotalOxygenConsumptionPerMinutes()).thenReturn(ANY_CREW_OXYGEN_CONSUMPTION_PER_MINUTES);
        Oxygen expectedRequiredOxygen = new Oxygen(5850);

        Oxygen calculatedRequiredOxygen = overconsumptionOxygenCalculator.calculateRequiredOxygen(cruiseBookingsMock, crewMock);

        assertEquals(expectedRequiredOxygen, calculatedRequiredOxygen);
    }
}