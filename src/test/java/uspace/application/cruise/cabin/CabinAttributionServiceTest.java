package uspace.application.cruise.cabin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.cabin.dtos.CabinsAttributionDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.cabin.CabinAttribution;
import uspace.domain.cruise.cabin.CabinAttributionCriteria;
import uspace.domain.cruise.cabin.CabinAttributor;
import uspace.domain.cruise.cabin.CabinId;
import uspace.domain.cruise.cabin.CabinRegistry;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CabinAttributionServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final CabinAttributionCriteria ANY_CRITERIA = CabinAttributionCriteria.BOOKING_DATE_TIME;

    private static final Map<CabinType, List<CabinId>> ANY_CABIN_IDS_BY_CATEGORY = Map.of(
            CabinType.STANDARD, List.of(new CabinId("A1"))
    );

    private CabinAttributionService cabinAttributionService;

    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private CabinRegistry cabinRegistryMock;
    @Mock
    private CabinAttributor cabinAttributorMock;
    @Mock
    private CabinAttributionAssembler assemblerMock;
    @Mock
    private Cruise cruiseMock;

    @BeforeEach
    void createCabinAttributionService() {
        cabinAttributionService = new CabinAttributionService(
                cruiseRepositoryMock, cabinRegistryMock, cabinAttributorMock, assemblerMock);
    }

    @Test
    void givenNonExistingCruise_whenGetCabinAttributions_thenThrowCruiseNotFoundException() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);

        assertThrows(CruiseNotFoundException.class,
                () -> cabinAttributionService.getCabinAttributions(ANY_CRUISE_ID_STR, ANY_CRITERIA));
    }

    @Test
    void givenValidRequest_whenGetCabinAttributions_thenDelegatesToCruiseAndReturnsDto() {
        List<CabinAttribution> anyAttributions = List.of();
        CabinsAttributionDto expectedDto = new CabinsAttributionDto(List.of());
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
        when(cabinRegistryMock.findCabinIdsByCategory()).thenReturn(ANY_CABIN_IDS_BY_CATEGORY);
        when(cruiseMock.generateAttributions(cabinAttributorMock, ANY_CABIN_IDS_BY_CATEGORY, ANY_CRITERIA)).thenReturn(anyAttributions);
        when(assemblerMock.toDto(anyAttributions)).thenReturn(expectedDto);

        CabinsAttributionDto result = cabinAttributionService.getCabinAttributions(ANY_CRUISE_ID_STR, ANY_CRITERIA);

        assertSame(expectedDto, result);
    }
}
