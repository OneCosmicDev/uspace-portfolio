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
class StandardOxygenCalculatorTest {

    private static final double ANY_REMAINING_TIME_IN_MINUTES = 2;
    private static final Oxygen ANY_TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTES = new Oxygen(500);
    private static final Oxygen ANY_CREW_OXYGEN_CONSUMPTION_PER_MINUTES = new Oxygen(1400);
    private StandardOxygenCalculator standardOxygenCalculator;
    @Mock
    private CruiseBookings cruiseBookingsMock;
    @Mock
    private Crew crewMock;

    @BeforeEach
    void createStandardOxygenCalculator() {
        standardOxygenCalculator = new StandardOxygenCalculator(ANY_REMAINING_TIME_IN_MINUTES);
    }

    @Test
    void whenCalculateRequiredOxygen_thenReturnCrewAndTravelersConsumptionForRemainingTime() {
        when(cruiseBookingsMock.getTotalOxygenConsumptionPerMinutes()).thenReturn(ANY_TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTES);
        when(crewMock.getTotalOxygenConsumptionPerMinutes()).thenReturn(ANY_CREW_OXYGEN_CONSUMPTION_PER_MINUTES);
        Oxygen expectedRequiredOxygen = new Oxygen(3800);

        Oxygen calculatedRequiredOxygen = standardOxygenCalculator.calculateRequiredOxygen(cruiseBookingsMock, crewMock);

        assertEquals(expectedRequiredOxygen, calculatedRequiredOxygen);
    }
}