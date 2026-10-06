package uspace.application.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.oxygen.RequiredOxygenAssembler;
import uspace.application.cruise.oxygen.dtos.RequiredOxygenDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.oxygen.Oxygen;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CruiseServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final String ANY_CALCULATION_DATE_TIME = "2084-04-08T00:00";
    private static final LocalDateTime ANY_CALCULATION_DATE_TIME_PARSED = LocalDateTime.of(2024,4,8,0,0);
    private static final CruiseDateTime ANY_CALCULATION_CRUISE_DATE_TIME = new CruiseDateTime(ANY_CALCULATION_DATE_TIME_PARSED);
    private static final int ANY_REQUIRED_OXYGEN_NUMBER = 1500;
    private static final Oxygen ANY_REQUIRED_OXYGEN = new Oxygen(ANY_REQUIRED_OXYGEN_NUMBER);
    private CruiseService cruiseService;
    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private CruiseAssembler cruiseAssemblerMock;
    @Mock
    private LocalDateTimeParser localDateTimeParserMock;
    @Mock
    private Cruise cruiseMock;

    @BeforeEach
    void createCruiseService() {
        RequiredOxygenAssembler requiredOxygenAssembler = new RequiredOxygenAssembler();
        cruiseService = new CruiseService(cruiseRepositoryMock, cruiseAssemblerMock, localDateTimeParserMock, requiredOxygenAssembler);
    }

    @Test
    void givenNonExistingCruise_whenGetRequiredOxygen_thenThrowCruiseNotFoundException(){
        givenNonExistingCruise();

        Executable getRequiredOxygen = () -> cruiseService.getRequiredOxygen(ANY_CRUISE_ID_STR, ANY_CALCULATION_DATE_TIME);

        assertThrows(CruiseNotFoundException.class, getRequiredOxygen);
    }

    @Test
    void givenExistingCruise_whenGetRequiredOxygen_thenReturnCalculatedRequiredOxygenOnCruise(){
        givenExistingCruise();
        givenRequiredOxygenOnCruise();

        RequiredOxygenDto requiredOxygenDto = cruiseService.getRequiredOxygen(ANY_CRUISE_ID_STR, ANY_CALCULATION_DATE_TIME);

        assertEquals(ANY_REQUIRED_OXYGEN_NUMBER, requiredOxygenDto.requiredOxygen);
    }


    private void givenNonExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
    }

    private void givenRequiredOxygenOnCruise() {
        when(localDateTimeParserMock.parse(ANY_CALCULATION_DATE_TIME)).thenReturn(ANY_CALCULATION_DATE_TIME_PARSED);
        when(cruiseMock.calculateRequiredOxygen(ANY_CALCULATION_CRUISE_DATE_TIME)).thenReturn(ANY_REQUIRED_OXYGEN);
    }
}