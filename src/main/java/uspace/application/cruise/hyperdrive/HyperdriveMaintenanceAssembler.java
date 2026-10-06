package uspace.application.cruise.hyperdrive;

import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceDuration;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenanceId;

import java.util.List;

public class HyperdriveMaintenanceAssembler {
    public HyperdriveMaintenance toHyperdriveMaintenance(HyperdriveMaintenanceDto hyperdriveMaintenanceDto) {
        List<EmployeeId> employeeIds = hyperdriveMaintenanceDto.employeeIds.stream().map(EmployeeId::new).toList();

        return new HyperdriveMaintenance(new HyperdriveMaintenanceId(),
                                        new HyperdriveMaintenanceDuration(hyperdriveMaintenanceDto.duration),
                                         employeeIds);
    }

    public HyperdriveMaintenanceDto toDto(HyperdriveMaintenance hyperdriveMaintenance) {
        List<String> employeeIds = hyperdriveMaintenance.getEmployeeIds().stream().map(EmployeeId::toString).toList();

        return new HyperdriveMaintenanceDto(hyperdriveMaintenance.getDuration().toInt(),
                                         employeeIds);
    }
}
