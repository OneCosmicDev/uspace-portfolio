package uspace.application.cruise.hyperdrive;

import jakarta.inject.Inject;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.application.cruise.hyperdrive.dtos.HyperdriveModuleDto;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;

import java.util.List;

public class HyperdriveModuleAssembler {
    private final HyperdriveMaintenanceAssembler hyperdriveMaintenanceAssembler;

    @Inject
    public HyperdriveModuleAssembler(HyperdriveMaintenanceAssembler hyperdriveMaintenanceAssembler) {
        this.hyperdriveMaintenanceAssembler = hyperdriveMaintenanceAssembler;
    }

    public HyperdriveModuleDto toDto(HyperdriveModule module) {
        List<HyperdriveMaintenanceDto> maintenanceDtos = createMaintenanceDtos(module.getMaintenances());

        return new HyperdriveModuleDto(module.getId().toString(),
                                       module.getPowerLevel().toInt(),
                                       module.getActivationDateTime().toString(),
                                       module.getDeactivationDateTime().toString(),
                                       maintenanceDtos);
    }

    private List<HyperdriveMaintenanceDto> createMaintenanceDtos(List<HyperdriveMaintenance> maintenances) {
        return maintenances.stream().map(hyperdriveMaintenanceAssembler::toDto).toList();
    }
}
