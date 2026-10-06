package uspace.api.cruise.hyperdrive;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.hyperdrive.HyperdriveModuleService;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;

@Path("/cruises/{cruiseId}/hyperdrive-modules")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HyperdriveModuleResource {

    private final HyperdriveModuleService hyperdriveModuleService;
    private final NewHyperdriveModuleValidator newHyperdriveModuleValidator;
    private final HyperdriveMaintenanceValidator hyperdriveMaintenanceValidator;

    @Inject
    public HyperdriveModuleResource(HyperdriveModuleService hyperdriveModuleService, NewHyperdriveModuleValidator newHyperdriveModuleValidator,
                                    HyperdriveMaintenanceValidator hyperdriveMaintenanceValidator) {
        this.hyperdriveModuleService = hyperdriveModuleService;
        this.newHyperdriveModuleValidator = newHyperdriveModuleValidator;
        this.hyperdriveMaintenanceValidator = hyperdriveMaintenanceValidator;
    }

    @POST
    public Response addHyperdriveModule(
            @PathParam("cruiseId") String cruiseId,
            NewHyperdriveModuleDto newHyperdriveModuleDto) {
        newHyperdriveModuleValidator.validate(newHyperdriveModuleDto);

        hyperdriveModuleService.addHyperdriveModule(cruiseId, newHyperdriveModuleDto);

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/{moduleId}/maintenances")
    public Response addHyperdriveMaintenance(
            @PathParam("cruiseId") String cruiseId,
            @PathParam("moduleId") String moduleId,
            HyperdriveMaintenanceDto hyperdriveMaintenanceDto) {
        hyperdriveMaintenanceValidator.validate(hyperdriveMaintenanceDto);

        hyperdriveModuleService.addHyperdriveMaintenance(cruiseId, moduleId, hyperdriveMaintenanceDto);

        return Response.status(Response.Status.CREATED).build();
    }
}
