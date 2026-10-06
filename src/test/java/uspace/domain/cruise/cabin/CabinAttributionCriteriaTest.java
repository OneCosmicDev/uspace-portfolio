package uspace.domain.cruise.cabin;

import org.junit.jupiter.api.Test;
import uspace.domain.cruise.cabin.exceptions.InvalidCabinAttributionCriteriaException;

import static org.junit.jupiter.api.Assertions.*;

class CabinAttributionCriteriaTest {

    @Test
    void givenBookingDateTimeString_whenFromString_thenReturnBookingDateTimeCriteria() {
        CabinAttributionCriteria criteria = CabinAttributionCriteria.fromString("bookingDateTime");

        assertEquals(CabinAttributionCriteria.BOOKING_DATE_TIME, criteria);
    }

    @Test
    void givenTravelersString_whenFromString_thenReturnTravelersCriteria() {
        CabinAttributionCriteria criteria = CabinAttributionCriteria.fromString("travelers");

        assertEquals(CabinAttributionCriteria.TRAVELERS, criteria);
    }

    @Test
    void givenInvalidString_whenFromString_thenThrowInvalidCabinAttributionCriteriaException() {
        assertThrows(InvalidCabinAttributionCriteriaException.class,
                () -> CabinAttributionCriteria.fromString("invalid"));
    }

    @Test
    void givenNullString_whenFromString_thenThrowInvalidCabinAttributionCriteriaException() {
        assertThrows(InvalidCabinAttributionCriteriaException.class,
                () -> CabinAttributionCriteria.fromString(null));
    }
}
