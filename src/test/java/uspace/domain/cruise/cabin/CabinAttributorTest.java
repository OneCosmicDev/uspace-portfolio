package uspace.domain.cruise.cabin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CabinAttributorTest {

    private static final CabinId FIRST_STANDARD_CABIN = new CabinId("A1");
    private static final CabinId SECOND_STANDARD_CABIN = new CabinId("A2");
    private static final CabinId FIRST_DELUXE_CABIN = new CabinId("K1");

    private static final Map<CabinType, List<CabinId>> ANY_CABIN_IDS_BY_CATEGORY = Map.of(
            CabinType.STANDARD, List.of(FIRST_STANDARD_CABIN, SECOND_STANDARD_CABIN),
            CabinType.DELUXE, List.of(FIRST_DELUXE_CABIN)
    );

    private static final CruiseDateTime EARLIER_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2084, 1, 1, 10, 0));
    private static final CruiseDateTime LATER_DATE_TIME = new CruiseDateTime(LocalDateTime.of(2084, 1, 2, 10, 0));

    private static final int ONE_TRAVELER = 1;
    private static final int TWO_TRAVELERS = 2;

    private CabinAttributor cabinAttributor;

    @BeforeEach
    void createCabinAttributor() {
        cabinAttributor = new CabinAttributor();
    }

    @Test
    void givenNoBookings_whenAttribute_thenReturnEmptyList() {
        List<CabinAttribution> result = cabinAttributor.attribute(List.of(), ANY_CABIN_IDS_BY_CATEGORY, CabinAttributionCriteria.BOOKING_DATE_TIME);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenTwoStandardBookingsInReverseOrder_whenAttributeByBookingDateTime_thenAssignCabinsInChronologicalOrder() {
        BookingId earlierBookingId = new BookingId();
        BookingId laterBookingId = new BookingId();
        Booking earlierBooking = bookingMock(earlierBookingId, CabinType.STANDARD);
        Booking laterBooking = bookingMock(laterBookingId, CabinType.STANDARD);
        when(earlierBooking.getBookingDateTime()).thenReturn(EARLIER_DATE_TIME);
        when(laterBooking.getBookingDateTime()).thenReturn(LATER_DATE_TIME);

        List<CabinAttribution> result = cabinAttributor.attribute(
                List.of(laterBooking, earlierBooking), ANY_CABIN_IDS_BY_CATEGORY, CabinAttributionCriteria.BOOKING_DATE_TIME);

        assertEquals(2, result.size());
        assertEquals(FIRST_STANDARD_CABIN, result.get(0).getCabinId());
        assertEquals(earlierBookingId, result.get(0).getBookingId());
        assertEquals(SECOND_STANDARD_CABIN, result.get(1).getCabinId());
        assertEquals(laterBookingId, result.get(1).getBookingId());
    }

    @Test
    void givenBookingsWithDifferentTravelerCounts_whenAttributeByTravelers_thenAssignCabinsInDescendingTravelerCountOrder() {
        BookingId bookingWith1TravelerId = new BookingId();
        BookingId bookingWith2TravelersId = new BookingId();
        Booking bookingWith1Traveler = bookingMock(bookingWith1TravelerId, CabinType.STANDARD);
        Booking bookingWith2Travelers = bookingMock(bookingWith2TravelersId, CabinType.STANDARD);
        when(bookingWith1Traveler.getTravelerCount()).thenReturn(ONE_TRAVELER);
        when(bookingWith2Travelers.getTravelerCount()).thenReturn(TWO_TRAVELERS);

        List<CabinAttribution> result = cabinAttributor.attribute(
                List.of(bookingWith1Traveler, bookingWith2Travelers), ANY_CABIN_IDS_BY_CATEGORY, CabinAttributionCriteria.TRAVELERS);

        assertEquals(2, result.size());
        assertEquals(FIRST_STANDARD_CABIN, result.get(0).getCabinId());
        assertEquals(bookingWith2TravelersId, result.get(0).getBookingId());
        assertEquals(SECOND_STANDARD_CABIN, result.get(1).getCabinId());
        assertEquals(bookingWith1TravelerId, result.get(1).getBookingId());
    }

    @Test
    void givenBookingsWithSameTravelerCount_whenAttributeByTravelers_thenAssignCabinsInChronologicalOrder() {
        BookingId earlierBookingId = new BookingId();
        BookingId laterBookingId = new BookingId();
        Booking earlierBooking = bookingMock(earlierBookingId, CabinType.STANDARD);
        Booking laterBooking = bookingMock(laterBookingId, CabinType.STANDARD);
        when(earlierBooking.getTravelerCount()).thenReturn(TWO_TRAVELERS);
        when(laterBooking.getTravelerCount()).thenReturn(TWO_TRAVELERS);
        when(earlierBooking.getBookingDateTime()).thenReturn(EARLIER_DATE_TIME);
        when(laterBooking.getBookingDateTime()).thenReturn(LATER_DATE_TIME);

        List<CabinAttribution> result = cabinAttributor.attribute(
                List.of(laterBooking, earlierBooking), ANY_CABIN_IDS_BY_CATEGORY, CabinAttributionCriteria.TRAVELERS);

        assertEquals(FIRST_STANDARD_CABIN, result.get(0).getCabinId());
        assertEquals(earlierBookingId, result.get(0).getBookingId());
    }

    @Test
    void givenFewerBookingsThanCabins_whenAttribute_thenOnlyReturnAttributedCabins() {
        List<CabinAttribution> result = cabinAttributor.attribute(
                List.of(bookingMock(new BookingId(), CabinType.STANDARD)), ANY_CABIN_IDS_BY_CATEGORY, CabinAttributionCriteria.BOOKING_DATE_TIME);

        assertEquals(1, result.size());
        assertEquals(FIRST_STANDARD_CABIN, result.get(0).getCabinId());
    }

    @Test
    void givenBookingsOfDifferentCategories_whenAttribute_thenAssignCabinsPerCategory() {
        List<CabinAttribution> result = cabinAttributor.attribute(
                List.of(bookingMock(new BookingId(), CabinType.STANDARD), bookingMock(new BookingId(), CabinType.DELUXE)),
                ANY_CABIN_IDS_BY_CATEGORY, CabinAttributionCriteria.BOOKING_DATE_TIME);

        assertEquals(2, result.size());
        CabinAttribution standardAttribution = result.stream().filter(c -> c.getCabinType() == CabinType.STANDARD).findFirst().orElseThrow();
        CabinAttribution deluxeAttribution = result.stream().filter(c -> c.getCabinType() == CabinType.DELUXE).findFirst().orElseThrow();
        assertEquals(FIRST_STANDARD_CABIN, standardAttribution.getCabinId());
        assertEquals(FIRST_DELUXE_CABIN, deluxeAttribution.getCabinId());
    }

    private Booking bookingMock(BookingId bookingId, CabinType cabinType) {
        Booking booking = mock(Booking.class);
        when(booking.getId()).thenReturn(bookingId);
        when(booking.getCabinType()).thenReturn(cabinType);
        return booking;
    }
}
