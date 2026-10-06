package uspace.api.exceptions.mappers.uspace;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.oxygen.exceptions.OxygenCalculationDateOutsideCruiseException;

@Provider
public class OxygenCalculationDateOutsideCruiseExceptionMapper implements ExceptionMapper<OxygenCalculationDateOutsideCruiseException> {

    @Override
    public Response toResponse(OxygenCalculationDateOutsideCruiseException exception) {
        ErrorResponse error = new ErrorResponse("OXYGEN_CALCULATION_DATE_OUTSIDE_CRUISE", exception.getMessage());
        return Response.status(400).entity(error).build();
    }
}
