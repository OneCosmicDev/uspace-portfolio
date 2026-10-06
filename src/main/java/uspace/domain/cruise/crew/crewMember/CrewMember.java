package uspace.domain.cruise.crew.crewMember;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.oxygen.Oxygen;

@Entity
public class CrewMember {
    @Id
    private EmployeeId employeeId;

    private CrewMemberName name;

    private Oxygen oxygenConsumptionPerMinute;

    public CrewMember() {
    }

    public CrewMember(EmployeeId employeeId, CrewMemberName name, Oxygen oxygenConsumptionPerMinute) {
        this.employeeId = employeeId;
        this.name = name;
        this.oxygenConsumptionPerMinute = oxygenConsumptionPerMinute;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public CrewMemberName getName() {
        return name;
    }

    public Oxygen getOxygenConsumptionPerMinute() {
        return oxygenConsumptionPerMinute;
    }
}
