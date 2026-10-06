package uspace.api.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HyperdriveMaintenanceValidatorTest {

    private static final int ANY_POSITIVE_DURATION = 4;
    private static final List<String> ANY_EMPLOYEE_IDS = List.of("AN_EMPLOYEE_ID");
    private HyperdriveMaintenanceValidator hyperdriveMaintenanceValidator;

    @BeforeEach
    void createHyperdriveMaintenanceValidator() {
        hyperdriveMaintenanceValidator = new HyperdriveMaintenanceValidator();
    }

    @Test
    void givenHyperdriveMaintenanceDtoWithoutDuration_whenValidate_thenThrowMissingParameterException() {
        HyperdriveMaintenanceDto hyperdriveMaintenanceDto = new HyperdriveMaintenanceDto(0, ANY_EMPLOYEE_IDS);

        Executable validate = () -> hyperdriveMaintenanceValidator.validate(hyperdriveMaintenanceDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenHyperdriveMaintenanceDtoWithNegativeDuration_whenValidate_thenThrowInvalidParameterException() {
        int anyNegativeDuration = -5;
        HyperdriveMaintenanceDto hyperdriveMaintenanceDto = new HyperdriveMaintenanceDto(anyNegativeDuration, ANY_EMPLOYEE_IDS);

        Executable validate = () -> hyperdriveMaintenanceValidator.validate(hyperdriveMaintenanceDto);

        assertThrows(InvalidParameterException.class, validate);
    }

    @Test
    void givenHyperdriveMaintenanceDtoWithoutEmployeeIds_whenValidate_thenThrowMissingParameterException() {
        HyperdriveMaintenanceDto hyperdriveMaintenanceDto = new HyperdriveMaintenanceDto(ANY_POSITIVE_DURATION, null);

        Executable validate = () -> hyperdriveMaintenanceValidator.validate(hyperdriveMaintenanceDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenValidHyperdriveMaintenanceDto_whenValidate_thenDoNothing() {
        HyperdriveMaintenanceDto hyperdriveMaintenanceDto = new HyperdriveMaintenanceDto(ANY_POSITIVE_DURATION, ANY_EMPLOYEE_IDS);

        Executable validate = () -> hyperdriveMaintenanceValidator.validate(hyperdriveMaintenanceDto);

        assertDoesNotThrow(validate);
    }

}