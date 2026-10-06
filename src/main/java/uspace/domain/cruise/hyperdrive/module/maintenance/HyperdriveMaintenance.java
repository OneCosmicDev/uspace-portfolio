package uspace.domain.cruise.hyperdrive.module.maintenance;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import uspace.domain.cruise.crew.Crew;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.hyperdrive.module.HyperdrivePowerLevel;

import java.util.List;

@Entity
public class HyperdriveMaintenance {
    @Id
    private HyperdriveMaintenanceId id;
    private HyperdriveMaintenanceDuration duration;
    @ElementCollection(fetch = FetchType.EAGER)
    private  List<EmployeeId> employeeIds;

    public HyperdriveMaintenance() {}

    public HyperdriveMaintenance(HyperdriveMaintenanceId id, HyperdriveMaintenanceDuration duration, List<EmployeeId> employeeIds) {
        this.id = id;
        this.duration = duration;
        this.employeeIds = employeeIds;
    }

    public HyperdriveMaintenanceDuration getDuration() {
        return duration;
    }

    public List<EmployeeId> getEmployeeIds() {
        return employeeIds;
    }

    public boolean hasValidEmployees(Crew crew) {
        for (EmployeeId employeeId : employeeIds) {
            if (crew.findCrewMemberById(employeeId) == null) {
                return false;
            }
        }

        return true;
    }

    public boolean isDurationSufficient(HyperdrivePowerLevel powerLevel) {
        return duration.isGreaterOrEqualThan(new HyperdriveMaintenanceDuration(powerLevel.toInt()));
    }
}
