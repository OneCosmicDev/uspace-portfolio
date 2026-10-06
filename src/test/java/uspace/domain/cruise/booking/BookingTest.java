package uspace.domain.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.sport.Sport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingTest {
    private static final BookingId ANY_BOOKING_ID = new BookingId();
    private static final TravelerId ANY_TRAVELER_ID = new TravelerId();
    private static final CabinType ANY_CABIN_TYPE = CabinType.DELUXE;
    private static final CruiseDateTime ANY_BOOKING_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2222, 3, 4, 1, 2));
    private static final Sport ANY_SPORT = new Sport();
    private Booking booking;
    @Mock
    private Traveler travelerMock;
    @Mock
    private Traveler otherTravelerMock;

    @BeforeEach
    void createBooking() {
        List<Traveler> anyTravelers = new ArrayList<>(List.of(travelerMock, otherTravelerMock));
        booking = new Booking(ANY_BOOKING_ID, anyTravelers, ANY_CABIN_TYPE, ANY_BOOKING_DATE_TIME);
    }

    @Test
    void givenNoTraveler_whenGetTotalOxygenConsumptionPerMinutes_thenReturnZero() {
        Oxygen zeroOxygen = new Oxygen(0);
        List<Traveler> noTraveler = new ArrayList<>();
        Booking bookingWithoutTraveler = new Booking(ANY_BOOKING_ID, noTraveler, ANY_CABIN_TYPE, ANY_BOOKING_DATE_TIME);

        Oxygen totalOxygenConsumptionPerMinutes = bookingWithoutTraveler.getTotalOxygenConsumptionPerMinutes();

        assertEquals(zeroOxygen, totalOxygenConsumptionPerMinutes);
    }

    @Test
    void givenTravelers_whenGetTotalOxygenConsumptionPerMinutes_thenReturnTotalOxygenConsumptionForAllTravelers() {
        Oxygen anyOxygen = new Oxygen(100);
        Oxygen anyOtherOxygen = new Oxygen(555);
        Oxygen expectedTotalOxygenConsumption = new Oxygen(655);
        when(travelerMock.getOxygenConsumptionPerMinute()).thenReturn(anyOxygen);
        when(otherTravelerMock.getOxygenConsumptionPerMinute()).thenReturn(anyOtherOxygen);

        Oxygen totalOxygenConsumptionPerMinutes = booking.getTotalOxygenConsumptionPerMinutes();

        assertEquals(expectedTotalOxygenConsumption, totalOxygenConsumptionPerMinutes);
    }

    @Test
    void givenNonExistingTraveler_whenBookSport_thenThrowTravelerNotFoundException() {
        givenNonExistingTraveler();

        Executable action = () -> booking.bookSport(ANY_TRAVELER_ID, ANY_SPORT);

        assertThrows(TravelerNotFoundException.class, action);
    }

    @Test
    void givenExistingTraveler_whenBookSport_thenTravelerBookSport() {
        givenExistingTraveler();

        booking.bookSport(ANY_TRAVELER_ID, ANY_SPORT);

        verify(travelerMock).bookSport(ANY_SPORT);
    }

    private void givenNonExistingTraveler() {
        booking = new Booking(ANY_BOOKING_ID, List.of(), ANY_CABIN_TYPE, ANY_BOOKING_DATE_TIME);
    }

    private void givenExistingTraveler() {
        when(travelerMock.getId()).thenReturn(ANY_TRAVELER_ID);
        booking = new Booking(ANY_BOOKING_ID, List.of(travelerMock), ANY_CABIN_TYPE, ANY_BOOKING_DATE_TIME);
    }
}