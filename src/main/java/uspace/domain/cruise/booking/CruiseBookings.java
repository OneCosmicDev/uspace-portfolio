package uspace.domain.cruise.booking;

import jakarta.persistence.*;
import uspace.domain.cruise.oxygen.Oxygen;

import java.util.List;
import java.util.Map;

@Entity
public class CruiseBookings {
    @Id
    private CruiseBookingsId id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "id")
    private Map<BookingId, Booking> bookings;

    public CruiseBookings() {
    }

    public CruiseBookings(CruiseBookingsId id, Map<BookingId, Booking> bookings) {
        this.id = id;
        this.bookings = bookings;
    }

    public void add(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    public Booking findById(BookingId bookingId) {
        return bookings.get(bookingId);
    }

    public List<Booking> getAll() {
        return bookings.values().stream().toList();
    }

    public Oxygen getTotalOxygenConsumptionPerMinutes() {
        Oxygen totalOxygenConsumption = new Oxygen(0);
        for (Booking booking : bookings.values()) {
            totalOxygenConsumption = totalOxygenConsumption.add(booking.getTotalOxygenConsumptionPerMinutes());
        }

        return totalOxygenConsumption;
    }
}
