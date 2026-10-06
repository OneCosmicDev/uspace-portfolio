package uspace.domain.cruise.oxygen.exceptions;

public class OxygenCalculationDateOutsideCruiseException extends RuntimeException {

    public OxygenCalculationDateOutsideCruiseException() {
        super("Calculation date must be within the cruise timeframe");
    }
}
