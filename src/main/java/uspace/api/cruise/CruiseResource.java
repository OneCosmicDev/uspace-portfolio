package uspace.api.cruise;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.api.cruise.oxygen.OxygenCalculationValidator;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.oxygen.dtos.RequiredOxygenDto;

@Path("/cruises")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CruiseResource {

    private final CruiseService cruiseService;
    private final OxygenCalculationValidator oxygenCalculationValidator;

    @Inject
    public CruiseResource(CruiseService cruiseService, OxygenCalculationValidator oxygenCalculationValidator) {
        this.cruiseService = cruiseService;
        this.oxygenCalculationValidator = oxygenCalculationValidator;
    }

    @GET
    @Path("{cruiseId}")
    public Response getCruise(@PathParam("cruiseId") String cruiseId) {
        CruiseDto cruiseDto = cruiseService.findCruise(cruiseId);
        return Response.ok(cruiseDto).build();
    }
    @GET
    @Path("{cruiseId}/oxygen")
    public Response getRequiredOxygen(@PathParam("cruiseId") String cruiseId, @QueryParam("calculationDateTime") String calculationDateTime) {
        oxygenCalculationValidator.validate(calculationDateTime);
        RequiredOxygenDto requiredOxygenDto = cruiseService.getRequiredOxygen(cruiseId, calculationDateTime);
        return Response.ok(requiredOxygenDto).build();
    }
}
