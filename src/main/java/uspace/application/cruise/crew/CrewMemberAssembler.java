package uspace.application.cruise.crew;

import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.crew.crewMember.CrewMemberName;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.oxygen.Oxygen;

public class CrewMemberAssembler {

    public CrewMember toCrewMember(CrewMemberDto crewMemberDto, Oxygen crewMemberOxygenConsumptionPerMinute) {
        return new CrewMember(new EmployeeId(crewMemberDto.employeeId), new CrewMemberName(crewMemberDto.name), crewMemberOxygenConsumptionPerMinute);
    }
}
