package uspace.domain.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.oxygen.Oxygen;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CruiseBookingsTest {

    private static final CruiseBookingsId ANY_CRUISE_BOOKINGS_ID = new CruiseBookingsId();
    private static final BookingId ANY_BOOKING_ID = new BookingId();
    private static final BookingId ANY_OTHER_BOOKING_ID = new BookingId();
    private CruiseBookings cruiseBookings;
    @Mock
    private Booking bookingMock;
    @Mock
    private Booking otherBookingMock;

    @BeforeEach
    void createCruiseBookings(){
        Map<BookingId, Booking> anyBookings = new HashMap<>(Map.of(
                ANY_BOOKING_ID, bookingMock,
                ANY_OTHER_BOOKING_ID, otherBookingMock
                                                                  ));
        cruiseBookings = new CruiseBookings(ANY_CRUISE_BOOKINGS_ID, anyBookings);
    }

    @Test
    void givenNoBooking_whenGetTotalOxygenConsumptionPerMinutes_thenReturnZero() {
        Oxygen zeroOxygen = new Oxygen(0);
        Map<BookingId, Booking> noBooking = new HashMap<>();
        CruiseBookings cruiseBookingsWithoutBooking = new CruiseBookings(ANY_CRUISE_BOOKINGS_ID, noBooking);

        Oxygen totalOxygenConsumptionPerMinutes = cruiseBookingsWithoutBooking.getTotalOxygenConsumptionPerMinutes();

        assertEquals(zeroOxygen, totalOxygenConsumptionPerMinutes);
    }

    @Test
    void givenBookings_whenGetTotalOxygenConsumptionPerMinutes_thenReturnTotalOxygenConsumptionForAllBookings() {
        Oxygen anyOxygen = new Oxygen(100);
        Oxygen anyOtherOxygen = new Oxygen(555);
        Oxygen expectedTotalOxygenConsumption = new Oxygen(655);
        when(bookingMock.getTotalOxygenConsumptionPerMinutes()).thenReturn(anyOxygen);
        when(otherBookingMock.getTotalOxygenConsumptionPerMinutes()).thenReturn(anyOtherOxygen);

        Oxygen totalOxygenConsumptionPerMinutes = cruiseBookings.getTotalOxygenConsumptionPerMinutes();

        assertEquals(expectedTotalOxygenConsumption, totalOxygenConsumptionPerMinutes);
    }

}