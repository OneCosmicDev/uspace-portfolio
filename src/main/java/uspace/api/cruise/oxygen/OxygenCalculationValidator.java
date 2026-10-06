package uspace.api.cruise.oxygen;

import uspace.domain.exceptions.MissingParameterException;

public class OxygenCalculationValidator {
    public void validate(String calculationDateTime) {

        if (calculationDateTime == null || calculationDateTime.isEmpty()) {
            throw new MissingParameterException("calculationDateTime");
        }
    }
}
