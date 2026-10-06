package uspace.domain.cruise.hyperdrive.module;

import jakarta.persistence.*;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.exceptions.InvalidHyperdriveMaintenanceDurationException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;

import java.util.List;

@Entity
public class HyperdriveModule {
    @Id
    private HyperdriveModuleId id;
    @Embedded
    private HyperdrivePowerLevel powerLevel;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "localDateTime",
                    column = @Column(name = "activationDateTime")
            ),
    })
    private CruiseDateTime activationDateTime;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "localDateTime",
                    column = @Column(name = "deactivationDateTime")
            ),
    })
    private CruiseDateTime deactivationDateTime;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<HyperdriveMaintenance> maintenances;

    public HyperdriveModule() {

    }

    public HyperdriveModule(HyperdriveModuleId id, HyperdrivePowerLevel powerLevel, CruiseDateTime activationDateTime,
                            CruiseDateTime deactivationDateTime, List<HyperdriveMaintenance> maintenances) {
        this.id = id;
        this.powerLevel = powerLevel;
        this.activationDateTime = activationDateTime;
        this.deactivationDateTime = deactivationDateTime;
        this.maintenances = maintenances;
    }

    public HyperdriveModuleId getId() {
        return id;
    }

    public HyperdrivePowerLevel getPowerLevel() {
        return powerLevel;
    }

    public CruiseDateTime getActivationDateTime() {
        return activationDateTime;
    }

    public CruiseDateTime getDeactivationDateTime() {
        return deactivationDateTime;
    }

    public List<HyperdriveMaintenance> getMaintenances() {
        return maintenances;
    }

    public boolean hasSameIdThan(HyperdriveModule hyperdriveModule) {
        return id.equals(hyperdriveModule.id);
    }

    public boolean isActivationBetween(CruiseDateTime startDateTime, CruiseDateTime endDateTime) {
        return activationDateTime.isAfter(startDateTime) && deactivationDateTime.isBefore(endDateTime);
    }

    public boolean hasSameActivationPeriodThan(HyperdriveModule hyperdriveModule) {
        return isDuringActivation(hyperdriveModule.activationDateTime) || isDuringActivation(hyperdriveModule.deactivationDateTime);
    }

    public void addMaintenance(HyperdriveMaintenance maintenance) {
        if (!maintenance.isDurationSufficient(powerLevel)) {
            throw new InvalidHyperdriveMaintenanceDurationException();
        }
        maintenances.add(maintenance);
    }

    private boolean isDuringActivation(CruiseDateTime dateTime) {
        return dateTime.isAfter(activationDateTime) && dateTime.isBefore(deactivationDateTime);
    }
}
