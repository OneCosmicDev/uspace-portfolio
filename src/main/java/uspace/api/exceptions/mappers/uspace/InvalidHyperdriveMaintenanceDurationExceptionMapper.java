package uspace.api.exceptions.mappers.uspace;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.hyperdrive.module.maintenance.exceptions.InvalidHyperdriveMaintenanceDurationException;

@Provider
public class InvalidHyperdriveMaintenanceDurationExceptionMapper implements ExceptionMapper<InvalidHyperdriveMaintenanceDurationException> {

    @Override
    public Response toResponse(InvalidHyperdriveMaintenanceDurationException exception) {
        ErrorResponse error = new ErrorResponse("INVALID_MAINTENANCE_DURATION", exception.getMessage());
        return Response.status(400).entity(error).build();
    }
}
