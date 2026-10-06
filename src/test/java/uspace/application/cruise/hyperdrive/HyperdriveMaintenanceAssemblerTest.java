package uspace.application.cruise.hyperdrive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceDuration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HyperdriveMaintenanceAssemblerTest {

    private static final int ANY_DURATION = 4;
    private static final String ANY_EMPLOYEE_ID = "AN_EMPLOYEE_ID";
    private static final String ANY_OTHER_EMPLOYEE_ID = "ANOTHER_EMPLOYEE_ID";
    private static final List<String> ANY_EMPLOYEE_IDS = List.of(ANY_EMPLOYEE_ID, ANY_OTHER_EMPLOYEE_ID);
    private static final HyperdriveMaintenanceDto ANY_HYPERDRIVE_MAINTENANCE_DTO = new HyperdriveMaintenanceDto(ANY_DURATION, ANY_EMPLOYEE_IDS);
    private HyperdriveMaintenanceAssembler hyperdriveMaintenanceAssembler;

    @BeforeEach
    void createHyperdriveMaintenanceAssembler() {
        hyperdriveMaintenanceAssembler = new HyperdriveMaintenanceAssembler();
    }

    @Test
    void whenAssembleHyperdriveMaintenance_thenReturnHyperdriveMaintenanceWithCorrectDuration() {
        HyperdriveMaintenanceDuration expectedDuration = new HyperdriveMaintenanceDuration(ANY_DURATION);

        HyperdriveMaintenance hyperdriveMaintenance = hyperdriveMaintenanceAssembler.toHyperdriveMaintenance(ANY_HYPERDRIVE_MAINTENANCE_DTO);

        assertEquals(expectedDuration, hyperdriveMaintenance.getDuration());
    }

    @Test
    void whenAssembleHyperdriveMaintenance_thenReturnHyperdriveMaintenanceWithCorrectEmployeeIds() {
        List<EmployeeId> expectedEmployeeIds = List.of(new EmployeeId(ANY_EMPLOYEE_ID), new EmployeeId(ANY_OTHER_EMPLOYEE_ID));

        HyperdriveMaintenance hyperdriveMaintenance = hyperdriveMaintenanceAssembler.toHyperdriveMaintenance(ANY_HYPERDRIVE_MAINTENANCE_DTO);

        assertEquals(expectedEmployeeIds, hyperdriveMaintenance.getEmployeeIds());
    }
}