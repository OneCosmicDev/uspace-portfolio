package uspace.domain.cruise.booking;

import jakarta.inject.Inject;
import uspace.domain.cruise.booking.newBooking.NewBooking;
import uspace.domain.cruise.booking.newBooking.NewBookingTraveler;
import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.booking.traveler.TravelerFactory;
import uspace.domain.cruise.booking.traveler.Traveler;

import java.util.List;

public class BookingFactory {
    private final TravelerFactory travelerFactory;

    @Inject
    public BookingFactory(TravelerFactory travelerFactory) {
        this.travelerFactory = travelerFactory;
    }

    public Booking create(NewBooking newBooking) {
        List<Traveler> travelers = createTravelers(newBooking.getTravelers());

        return new Booking(new BookingId(),
                           travelers,
                           newBooking.getCabinType(),
                           newBooking.getBookingDateTime());
    }

    private List<Traveler> createTravelers(List<NewBookingTraveler> travelers) {
        return travelers.stream().map(traveler -> travelerFactory.create(traveler.name(), TravelerCategory.fromString(traveler.category()))).toList();
    }
}
