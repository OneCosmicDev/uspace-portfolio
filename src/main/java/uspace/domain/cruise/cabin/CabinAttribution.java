package uspace.domain.cruise.cabin;

import uspace.domain.cruise.booking.BookingId;

public class CabinAttribution {

    private final CabinId cabinId;
    private final BookingId bookingId;
    private final CabinType cabinType;

    public CabinAttribution(CabinId cabinId, BookingId bookingId, CabinType cabinType) {
        this.cabinId = cabinId;
        this.bookingId = bookingId;
        this.cabinType = cabinType;
    }

    public CabinId getCabinId() {
        return cabinId;
    }

    public BookingId getBookingId() {
        return bookingId;
    }

    public CabinType getCabinType() {
        return cabinType;
    }
}
