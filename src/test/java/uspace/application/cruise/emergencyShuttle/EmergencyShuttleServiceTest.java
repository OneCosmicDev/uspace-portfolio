package uspace.application.cruise.emergencyShuttle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttlesManifestDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttleManifest;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmergencyShuttleServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);

    private EmergencyShuttleService emergencyShuttleService;

    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private EmergencyShuttleManifestAssembler assemblerMock;
    @Mock
    private Cruise cruiseMock;
    @Mock
    private EmergencyShuttleManifest manifestMock;

    @BeforeEach
    void createEmergencyShuttleService() {
        emergencyShuttleService = new EmergencyShuttleService(cruiseRepositoryMock, assemblerMock);
    }

    @Test
    void givenNonExistingCruise_whenGetEmergencyShuttleManifest_thenThrowCruiseNotFoundException() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);

        assertThrows(CruiseNotFoundException.class,
                () -> emergencyShuttleService.getEmergencyShuttleManifest(ANY_CRUISE_ID_STR));
    }

    @Test
    void givenExistingCruise_whenGetEmergencyShuttleManifest_thenReturnManifestDto() {
        EmergencyShuttlesManifestDto expectedDto = new EmergencyShuttlesManifestDto(0, List.of());
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
        when(cruiseMock.getEmergencyShuttleManifest()).thenReturn(manifestMock);
        when(assemblerMock.toDto(manifestMock)).thenReturn(expectedDto);

        EmergencyShuttlesManifestDto result = emergencyShuttleService.getEmergencyShuttleManifest(ANY_CRUISE_ID_STR);

        assertSame(expectedDto, result);
    }
}
