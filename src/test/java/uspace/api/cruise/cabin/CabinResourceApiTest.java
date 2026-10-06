package uspace.api.cruise.cabin;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.ErrorResponse;
import uspace.application.cruise.cabin.CabinAttributionService;
import uspace.application.cruise.cabin.dtos.CabinAttributionDto;
import uspace.application.cruise.cabin.dtos.CabinsAttributionDto;
import uspace.domain.cruise.cabin.CabinAttributionCriteria;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("component")
class CabinResourceApiTest extends JerseyTest {

    private static final String ANY_CRUISE_ID = "ANY_CRUISE_ID";
    private static final String CRITERIA_BOOKING_DATE_TIME = "bookingDateTime";
    private static final String INVALID_CRITERIA = "invalid";

    private CabinAttributionService cabinAttributionServiceMock;

    @Override
    protected Application configure() {
        cabinAttributionServiceMock = mock(CabinAttributionService.class);

        return new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(new CabinResource(cabinAttributionServiceMock));
    }

    @Test
    void givenValidRequest_whenGetCabinAttributions_thenReturn200WithCabinsList() {
        CabinAttributionDto attribution = new CabinAttributionDto("A1", "booking-1", "STANDARD");
        when(cabinAttributionServiceMock.getCabinAttributions(ANY_CRUISE_ID, CabinAttributionCriteria.BOOKING_DATE_TIME))
                .thenReturn(new CabinsAttributionDto(List.of(attribution)));

        Response response = getCabinAttributions(ANY_CRUISE_ID, CRITERIA_BOOKING_DATE_TIME);

        assertEquals(200, response.getStatus());
        CabinsAttributionDto dto = response.readEntity(CabinsAttributionDto.class);
        assertEquals(1, dto.cabins.size());
        assertEquals("A1", dto.cabins.get(0).cabinId);
    }

    @Test
    void givenCruiseNotFound_whenGetCabinAttributions_thenReturn404WithCruiseNotFoundError() {
        when(cabinAttributionServiceMock.getCabinAttributions(ANY_CRUISE_ID, CabinAttributionCriteria.BOOKING_DATE_TIME))
                .thenThrow(new CruiseNotFoundException());

        Response response = getCabinAttributions(ANY_CRUISE_ID, CRITERIA_BOOKING_DATE_TIME);

        assertEquals(404, response.getStatus());
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertEquals("CRUISE_NOT_FOUND", error.error);
    }

    @Test
    void givenInvalidCriteria_whenGetCabinAttributions_thenReturn400WithInvalidCriteriaError() {
        Response response = getCabinAttributions(ANY_CRUISE_ID, INVALID_CRITERIA);

        assertEquals(400, response.getStatus());
        ErrorResponse error = response.readEntity(ErrorResponse.class);
        assertEquals("INVALID_CABIN_ATTRIBUTION_CRITERIA", error.error);
    }

    private Response getCabinAttributions(String cruiseId, String criteria) {
        WebTarget target = target("/cruises/" + cruiseId + "/cabins").queryParam("criteria", criteria);
        return target.request().get();
    }
}
