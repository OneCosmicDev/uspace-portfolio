package uspace.domain.cruise.hyperdrive.module.maintenance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.crew.Crew;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceDuration;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceId;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HyperdriveMaintenanceTest {
    private static final HyperdriveMaintenanceId ANY_ID = new HyperdriveMaintenanceId();
    private static final HyperdriveMaintenanceDuration ANY_DURATION = new HyperdriveMaintenanceDuration(4);
    private static final EmployeeId ANY_EMPLOYEE_ID = new EmployeeId("anyId");
    private static final EmployeeId ANY_OTHER_EMPLOYEE_ID = new EmployeeId("anyOtherId");
    private static final List<EmployeeId> ANY_EMPLOYEE_IDS = List.of(ANY_EMPLOYEE_ID, ANY_OTHER_EMPLOYEE_ID);
    private static final CrewMember ANY_CREW_MEMBER = new CrewMember();
    private HyperdriveMaintenance hyperdriveMaintenance;
    @Mock
    private Crew crewMock;

    @BeforeEach
    void createHyperdriveMaintenance() {
        hyperdriveMaintenance = new HyperdriveMaintenance(ANY_ID, ANY_DURATION, ANY_EMPLOYEE_IDS);
    }

    @Test
    void givenAtLeastOneEmployeeIdNotPresentInCrew_whenValidateIfHasValidEmployees_thenReturnFalse(){
        when(crewMock.findCrewMemberById(ANY_EMPLOYEE_ID)).thenReturn(ANY_CREW_MEMBER);
        when(crewMock.findCrewMemberById(ANY_OTHER_EMPLOYEE_ID)).thenReturn(null);

        boolean hasValidEmployees = hyperdriveMaintenance.hasValidEmployees(crewMock);

        assertFalse(hasValidEmployees);
    }

    @Test
    void givenAllEmployeeIdsPresentInCrew_whenValidateIfHasValidEmployees_thenReturnTrue(){
        when(crewMock.findCrewMemberById(ANY_EMPLOYEE_ID)).thenReturn(ANY_CREW_MEMBER);
        when(crewMock.findCrewMemberById(ANY_OTHER_EMPLOYEE_ID)).thenReturn(ANY_CREW_MEMBER);

        boolean hasValidEmployees = hyperdriveMaintenance.hasValidEmployees(crewMock);

        assertTrue(hasValidEmployees);
    }

    @Test
    void givenPowerLevelGreaterThanDuration_whenValidateIfDurationIsSufficient_thenReturnFalse(){
        HyperdrivePowerLevel anyPowerLevelGreaterThanDuration = new HyperdrivePowerLevel(5);

        boolean isDurationSufficient = hyperdriveMaintenance.isDurationSufficient(anyPowerLevelGreaterThanDuration);

        assertFalse(isDurationSufficient);
    }

    @Test
    void givenPowerLevelEqualToDuration_whenValidateIfDurationIsSufficient_thenReturnTrue(){
        HyperdrivePowerLevel anyPowerLevelEqualDuration = new HyperdrivePowerLevel(4);

        boolean isDurationSufficient = hyperdriveMaintenance.isDurationSufficient(anyPowerLevelEqualDuration);

        assertTrue(isDurationSufficient);
    }

    @Test
    void givenPowerLevelLessThanDuration_whenValidateIfDurationIsSufficient_thenReturnTrue(){
        HyperdrivePowerLevel anyPowerLessThanDuration = new HyperdrivePowerLevel(3);

        boolean isDurationSufficient = hyperdriveMaintenance.isDurationSufficient(anyPowerLessThanDuration);

        assertTrue(isDurationSufficient);
    }
}