package uspace.domain.cruise.hyperdrive.module;

import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HyperdriveModuleFactory {
    private final LocalTime activationTime;

    public HyperdriveModuleFactory(LocalTime activationTime) {
        this.activationTime = activationTime;
    }

    public HyperdriveModule create(String id, int powerLevel, int activationDays, LocalDate activationDate) {
        LocalDateTime activationDateTime = activationDate.atTime(activationTime);
        LocalDateTime deactivationDateTime = activationDateTime.plusDays(activationDays);
        List<HyperdriveMaintenance> initialMaintenances = new ArrayList<>();

        return new HyperdriveModule(new HyperdriveModuleId(id), new HyperdrivePowerLevel(powerLevel), new CruiseDateTime(activationDateTime), new CruiseDateTime(deactivationDateTime), initialMaintenances);
    }
}
