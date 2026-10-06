package uspace.api.cruise.emergencyShuttle;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.emergencyShuttle.EmergencyShuttleService;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttlesManifestDto;

@Path("/cruises/{cruiseId}/emergencyShuttles")
@Produces(MediaType.APPLICATION_JSON)
public class EmergencyShuttleResource {

    private final EmergencyShuttleService emergencyShuttleService;

    @Inject
    public EmergencyShuttleResource(EmergencyShuttleService emergencyShuttleService) {
        this.emergencyShuttleService = emergencyShuttleService;
    }

    @GET
    public Response getEmergencyShuttleManifest(@PathParam("cruiseId") String cruiseId) {
        EmergencyShuttlesManifestDto dto = emergencyShuttleService.getEmergencyShuttleManifest(cruiseId);
        return Response.ok(dto).build();
    }
}
