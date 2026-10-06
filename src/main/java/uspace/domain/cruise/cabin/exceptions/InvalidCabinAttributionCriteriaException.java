package uspace.domain.cruise.cabin.exceptions;

public class InvalidCabinAttributionCriteriaException extends RuntimeException {

    public InvalidCabinAttributionCriteriaException() {
        super("Invalid cabin attribution criteria. It must be one of: bookingDateTime, travelers.");
    }
}
