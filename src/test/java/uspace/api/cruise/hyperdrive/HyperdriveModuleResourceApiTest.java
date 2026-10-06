package uspace.api.cruise.hyperdrive;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.application.cruise.hyperdrive.HyperdriveModuleService;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.hyperdrive.module.exceptions.HyperdriveModuleConflictException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleActivationDateException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleException;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleIdException;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("component")
class HyperdriveModuleResourceApiTest extends JerseyTest {
    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final String ANY_PARAMETER = "any";
    private static final NewHyperdriveModuleDto ANY_NEW_HYPERDRIVE_MODULE_DTO = new NewHyperdriveModuleDto();
    private HyperdriveModuleService hyperdriveModuleServiceMock;
    private NewHyperdriveModuleValidator newHyperdriveModuleValidatorMock;

    @Override
    protected Application configure() {
        hyperdriveModuleServiceMock = mock(HyperdriveModuleService.class);
        newHyperdriveModuleValidatorMock = mock(NewHyperdriveModuleValidator.class);
        HyperdriveMaintenanceValidator hyperdriveMaintenanceValidatorMock = mock(HyperdriveMaintenanceValidator.class);

        return new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(new HyperdriveModuleResource(hyperdriveModuleServiceMock, newHyperdriveModuleValidatorMock, hyperdriveMaintenanceValidatorMock));
    }

    @Test
    void givenNewHyperdriveModuleDtoWithMissingParameter_whenAddHyperdriveModule_thenReturnMissingParameterError() {
        doThrow(new MissingParameterException(ANY_PARAMETER)).when(newHyperdriveModuleValidatorMock).validate(any());

        Response response = callPostHyperdriveModule();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("MISSING_PARAMETER", errorResponse.error);
    }

    @Test
    void givenNewHyperdriveModuleDtoWithInvalidParameter_whenAddHyperdriveModule_thenReturnInvalidParameterError() {
        doThrow(new InvalidParameterException(ANY_PARAMETER)).when(newHyperdriveModuleValidatorMock).validate(any());

        Response response = callPostHyperdriveModule();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_PARAMETER", errorResponse.error);
    }

    @Test
    void givenCruiseNotFound_whenAddHyperdriveModule_thenReturnCruiseNotFoundError() {
        doThrow(new CruiseNotFoundException())
                .when(hyperdriveModuleServiceMock).addHyperdriveModule(eq(ANY_CRUISE_ID_STR), any());

        Response response = callPostHyperdriveModule();

        assertEquals(404, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("CRUISE_NOT_FOUND", errorResponse.error);
    }

    @Test
    void givenInvalidIdFormat_whenAddHyperdriveModule_thenReturnInvalidHyperdriveIdError() {
        doThrow(new InvalidHyperdriveModuleIdException())
                .when(hyperdriveModuleServiceMock).addHyperdriveModule(eq(ANY_CRUISE_ID_STR), any());

        Response response = callPostHyperdriveModule();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_HYPERDRIVE_ID_FORMAT", errorResponse.error);
    }

    @Test
    void givenModuleNotValidatedByExternalSystem_whenAddHyperdriveModule_thenReturnInvalidHyperdriveModuleError() {
        doThrow(new InvalidHyperdriveModuleException())
                .when(hyperdriveModuleServiceMock).addHyperdriveModule(eq(ANY_CRUISE_ID_STR), any());

        Response response = callPostHyperdriveModule();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_HYPERDRIVE_MODULE", errorResponse.error);
    }

    @Test
    void givenActivationDateNotDuringCruise_whenAddHyperdriveModule_thenReturnInvalidActivationDateError() {
        doThrow(new InvalidHyperdriveModuleActivationDateException())
                .when(hyperdriveModuleServiceMock).addHyperdriveModule(eq(ANY_CRUISE_ID_STR), any());

        Response response = callPostHyperdriveModule();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_ACTIVATION_DATE", errorResponse.error);
    }

    @Test
    void givenActivationDuringAnotherModuleActivationPeriod_whenAddHyperdriveModule_thenReturnModuleConflictError() {
        doThrow(new HyperdriveModuleConflictException())
                .when(hyperdriveModuleServiceMock).addHyperdriveModule(eq(ANY_CRUISE_ID_STR), any());

        Response response = callPostHyperdriveModule();

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertEquals("MODULE_CONFLICT", errorResponse.error);
    }

    private Response callPostHyperdriveModule() {
        WebTarget target = target("/cruises/" + ANY_CRUISE_ID_STR + "/hyperdrive-modules");
        return target.request().post(Entity.json(ANY_NEW_HYPERDRIVE_MODULE_DTO));
    }
}