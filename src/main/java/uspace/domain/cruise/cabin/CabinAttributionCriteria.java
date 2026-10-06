package uspace.domain.cruise.cabin;

import uspace.domain.cruise.cabin.exceptions.InvalidCabinAttributionCriteriaException;

public enum CabinAttributionCriteria {
    BOOKING_DATE_TIME("bookingDateTime"),
    TRAVELERS("travelers");

    private final String value;

    CabinAttributionCriteria(String value) {
        this.value = value;
    }

    public static CabinAttributionCriteria fromString(String value) {
        for (CabinAttributionCriteria criteria : CabinAttributionCriteria.values()) {
            if (criteria.value.equals(value)) {
                return criteria;
            }
        }

        throw new InvalidCabinAttributionCriteriaException();
    }
}
