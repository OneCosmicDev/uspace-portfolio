package uspace.api.cruise.emergencyShuttle;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.application.cruise.emergencyShuttle.EmergencyShuttleService;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttleDto;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttlesManifestDto;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("component")
class EmergencyShuttleResourceApiTest extends JerseyTest {

    private static final String ANY_CRUISE_ID = "ANY_CRUISE_ID";

    private EmergencyShuttleService emergencyShuttleServiceMock;

    @Override
    protected Application configure() {
        emergencyShuttleServiceMock = mock(EmergencyShuttleService.class);

        return new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(new EmergencyShuttleResource(emergencyShuttleServiceMock));
    }

    @Test
    void givenValidCruise_whenGetEmergencyShuttleManifest_thenReturn200WithManifest() {
        EmergencyShuttleDto shuttle = new EmergencyShuttleDto("RESCUE_SHIP", List.of(), List.of());
        when(emergencyShuttleServiceMock.getEmergencyShuttleManifest(ANY_CRUISE_ID))
                .thenReturn(new EmergencyShuttlesManifestDto(150000, List.of(shuttle)));

        Response response = getManifest(ANY_CRUISE_ID);

        assertEquals(200, response.getStatus());
        EmergencyShuttlesManifestDto dto = response.readEntity(EmergencyShuttlesManifestDto.class);
        assertEquals(150000, dto.cost);
        assertEquals(1, dto.emergencyShuttles.size());
        assertEquals("RESCUE_SHIP", dto.emergencyShuttles.get(0).type);
    }

    @Test
    void givenCruiseNotFound_whenGetEmergencyShuttleManifest_thenReturn404WithCruiseNotFoundError() {
        when(emergencyShuttleServiceMock.getEmergencyShuttleManifest(ANY_CRUISE_ID))
                .thenThrow(new CruiseNotFoundException());

        Response response = getManifest(ANY_CRUISE_ID);

        assertEquals(404, response.getStatus());
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertEquals("CRUISE_NOT_FOUND", error.error);
    }

    private Response getManifest(String cruiseId) {
        WebTarget target = target("/cruises/" + cruiseId + "/emergencyShuttles");
        return target.request().get();
    }
}
