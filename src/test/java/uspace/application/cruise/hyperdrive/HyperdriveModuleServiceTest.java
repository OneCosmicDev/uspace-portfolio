package uspace.application.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.application.cruise.hyperdrive.dtos.NewHyperdriveModuleDto;
import uspace.application.utils.dateTimeParser.LocalDateParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.hyperdrive.HyperdriveStabilityValidator;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceDuration;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceId;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModuleFactory;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HyperdriveModuleServiceTest {
    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final String ANY_HYPERDRIVE_MODULE_ID_STR = "ANY_HYPERDRIVE_MODULE_ID";
    private static final HyperdriveModuleId ANY_HYPERDRIVE_MODULE_ID = new HyperdriveModuleId(ANY_HYPERDRIVE_MODULE_ID_STR);
    private static final int ANY_POWER_LEVEL = 15;
    private static final int ANY_ACTIVATION_DAYS = 40;
    private static final String ANY_ACTIVATION_DATE = "1999-02-03";
    private static final LocalDate ANY_ACTIVATION_LOCAL_DATE = LocalDate.of(1999,2,3);
    private static final NewHyperdriveModuleDto ANY_NEW_HYPERDRIVE_MODULE_DTO = new NewHyperdriveModuleDto(ANY_HYPERDRIVE_MODULE_ID_STR, ANY_POWER_LEVEL, ANY_ACTIVATION_DAYS, ANY_ACTIVATION_DATE);
    private static final int ANY_DURATION = 4;
    private static final List<String> ANY_EMPLOYEE_IDS = List.of("AN_EMPLOYEE_ID");
    private static final HyperdriveMaintenanceDto ANY_HYPERDRIVE_MAINTENANCE_DTO = new HyperdriveMaintenanceDto(ANY_DURATION, ANY_EMPLOYEE_IDS);
    private static final HyperdriveMaintenance ANY_HYPERDRIVE_MAINTENANCE = new HyperdriveMaintenance(new HyperdriveMaintenanceId(), new HyperdriveMaintenanceDuration(ANY_DURATION), List.of(new EmployeeId()));
    private HyperdriveModuleService hyperdriveModuleService;
    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private HyperdriveStabilityValidator hyperdriveStabilityValidatorMock;
    @Mock
    private HyperdriveModuleFactory hyperdriveModuleFactoryMock;
    @Mock
    private LocalDateParser localDateParserMock;
    @Mock
    private Cruise cruiseMock;
    @Mock
    private HyperdriveModule hyperdriveModuleMock;
    @Mock
    private HyperdriveMaintenanceAssembler hyperdriveMaintenanceAssemblerMock;

    @BeforeEach
    void createHyperdriveModuleService() {
        hyperdriveModuleService = new HyperdriveModuleService(cruiseRepositoryMock, hyperdriveStabilityValidatorMock, hyperdriveModuleFactoryMock, localDateParserMock,
                                                              hyperdriveMaintenanceAssemblerMock);
    }

    @Test
    void givenNonExistingCruise_whenAddHyperdriveModule_thenThrowCruiseNotFoundException(){
        givenNonExistingCruise();

        Executable addHyperdriveModule = () -> hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        assertThrows(CruiseNotFoundException.class, addHyperdriveModule);
    }

    @Test
    void givenExistingCruiseAndHyperdriveModuleNotStable_whenAddHyperdriveModule_thenThrowInvalidHyperdriveModuleException(){
        givenExistingCruise();
        givenInvalidHyperdriveModule();

        Executable addHyperdriveModule = () -> hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        assertThrows(InvalidHyperdriveModuleException.class, addHyperdriveModule);
    }

    @Test
    void givenExistingCruiseAndValidHyperdriveModuleStable_whenAddHyperdriveModule_thenAddToCruise(){
        givenExistingCruise();
        givenValidHyperdriveModule();

        hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        verify(cruiseMock).addHyperdriveModule(hyperdriveModuleMock);
    }

    @Test
    void givenExistingCruiseAndValidHyperdriveModuleStable_whenAddHyperdriveModule_thenSaveCruise(){
        givenExistingCruise();
        givenValidHyperdriveModule();

        hyperdriveModuleService.addHyperdriveModule(ANY_CRUISE_ID_STR, ANY_NEW_HYPERDRIVE_MODULE_DTO);

        verify(cruiseRepositoryMock).save(cruiseMock);
    }

    @Test
    void givenNonExistingCruise_whenAddHyperdriveMaintenance_thenThrowCruiseNotFoundException(){
        givenNonExistingCruise();

        Executable addHyperdriveModule = () -> hyperdriveModuleService.addHyperdriveMaintenance(ANY_CRUISE_ID_STR, ANY_HYPERDRIVE_MODULE_ID_STR, ANY_HYPERDRIVE_MAINTENANCE_DTO);

        assertThrows(CruiseNotFoundException.class, addHyperdriveModule);
    }

    @Test
    void givenExistingCruise_whenAddHyperdriveMaintenance_thenAddHyperdriveMaintenanceToCruise(){
        givenExistingCruise();
        givenHyperdriveMaintenance();

        hyperdriveModuleService.addHyperdriveMaintenance(ANY_CRUISE_ID_STR, ANY_HYPERDRIVE_MODULE_ID_STR, ANY_HYPERDRIVE_MAINTENANCE_DTO);

        verify(cruiseMock).addHyperdriveMaintenance(ANY_HYPERDRIVE_MODULE_ID, ANY_HYPERDRIVE_MAINTENANCE);
    }

    @Test
    void givenExistingCruise_whenAddHyperdriveMaintenance_thenSaveCruise(){
        givenExistingCruise();
        givenHyperdriveMaintenance();

        hyperdriveModuleService.addHyperdriveMaintenance(ANY_CRUISE_ID_STR, ANY_HYPERDRIVE_MODULE_ID_STR, ANY_HYPERDRIVE_MAINTENANCE_DTO);

        verify(cruiseRepositoryMock).save(cruiseMock);
    }

    private void givenNonExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
    }

    private void givenInvalidHyperdriveModule() {
        when(hyperdriveStabilityValidatorMock.isHyperdriveModuleStable(ANY_HYPERDRIVE_MODULE_ID_STR)).thenReturn(false);
    }

    private void givenValidHyperdriveModule() {
        when(hyperdriveStabilityValidatorMock.isHyperdriveModuleStable(ANY_HYPERDRIVE_MODULE_ID_STR)).thenReturn(true);
        when(localDateParserMock.parse(ANY_ACTIVATION_DATE)).thenReturn(ANY_ACTIVATION_LOCAL_DATE);
        when(hyperdriveModuleFactoryMock.create(ANY_HYPERDRIVE_MODULE_ID_STR, ANY_POWER_LEVEL, ANY_ACTIVATION_DAYS, ANY_ACTIVATION_LOCAL_DATE)).thenReturn(hyperdriveModuleMock);
    }

    private void givenHyperdriveMaintenance() {
        when(hyperdriveMaintenanceAssemblerMock.toHyperdriveMaintenance(ANY_HYPERDRIVE_MAINTENANCE_DTO)).thenReturn(ANY_HYPERDRIVE_MAINTENANCE);
    }

}