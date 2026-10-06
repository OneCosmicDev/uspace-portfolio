package uspace.domain.cruise.cabin;

import uspace.domain.cruise.booking.Booking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CabinAttributor {

    public List<CabinAttribution> attribute(List<Booking> bookings,
                                             Map<CabinType, List<CabinId>> cabinIdsByCategory,
                                             CabinAttributionCriteria criteria) {
        List<CabinAttribution> attributions = new ArrayList<>();
        for (CabinType cabinType : CabinType.values()) {
            List<Booking> sortedBookings = getSortedBookingsForCabinType(bookings, cabinType, criteria);
            List<CabinId> cabinIds = cabinIdsByCategory.getOrDefault(cabinType, List.of());

            for (int i = 0; i < Math.min(sortedBookings.size(), cabinIds.size()); i++) {
                attributions.add(new CabinAttribution(cabinIds.get(i), sortedBookings.get(i).getId(), cabinType));
            }
        }
        return attributions;
    }

    private List<Booking> getSortedBookingsForCabinType(List<Booking> bookings, CabinType cabinType, CabinAttributionCriteria criteria) {
        return bookings.stream()
                .filter(b -> b.getCabinType() == cabinType)
                .sorted(comparatorFor(criteria))
                .toList();
    }

    private Comparator<Booking> comparatorFor(CabinAttributionCriteria criteria) {
        Comparator<Booking> byDateTime = (b1, b2) ->
                b1.getBookingDateTime().isBefore(b2.getBookingDateTime()) ? -1
                : b2.getBookingDateTime().isBefore(b1.getBookingDateTime()) ? 1 : 0;

        if (criteria == CabinAttributionCriteria.BOOKING_DATE_TIME) {
            return byDateTime;
        }
        return Comparator.comparingInt(Booking::getTravelerCount).reversed()
                .thenComparing(byDateTime);
    }
}