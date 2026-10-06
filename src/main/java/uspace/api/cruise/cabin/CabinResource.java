package uspace.api.cruise.cabin;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.cabin.CabinAttributionService;
import uspace.application.cruise.cabin.dtos.CabinsAttributionDto;
import uspace.domain.cruise.cabin.CabinAttributionCriteria;

@Path("/cruises/{cruiseId}/cabins")
@Produces(MediaType.APPLICATION_JSON)
public class CabinResource {

    private final CabinAttributionService cabinAttributionService;

    @Inject
    public CabinResource(CabinAttributionService cabinAttributionService) {
        this.cabinAttributionService = cabinAttributionService;
    }

    @GET
    public Response getCabinAttributions(
            @PathParam("cruiseId") String cruiseId,
            @QueryParam("criteria") String criteria) {
        CabinAttributionCriteria cabinAttributionCriteria = CabinAttributionCriteria.fromString(criteria);
        CabinsAttributionDto dto = cabinAttributionService.getCabinAttributions(cruiseId, cabinAttributionCriteria);
        return Response.ok(dto).build();
    }
}