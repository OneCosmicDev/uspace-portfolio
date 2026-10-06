package uspace.api.cruise.oxygen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OxygenCalculationValidatorTest {
    private OxygenCalculationValidator oxygenCalculationValidator;

    @BeforeEach
    void createOxygenCalculationValidator() {
        oxygenCalculationValidator = new OxygenCalculationValidator();
    }

    @Test
    void givenNoCalculationDateTime_whenValidate_thenThrowMissingParameterException() {
        String noCalculationDateTime = null;

        Executable validate = () -> oxygenCalculationValidator.validate(noCalculationDateTime);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenEmptyCalculationDateTime_whenValidate_thenThrowMissingParameterException() {
        String emptyCalculationDateTime = "";

        Executable validate = () -> oxygenCalculationValidator.validate(emptyCalculationDateTime);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenNotEmptyCalculationDateTime_whenValidate_thenDoNothing() {
        String anyNotEmptyCalculationDateTime = "anyCalculationDateTime";

        Executable validate = () -> oxygenCalculationValidator.validate(anyNotEmptyCalculationDateTime);

        assertDoesNotThrow(validate);
    }
}