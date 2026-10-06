package uspace.domain.cruise.emergencyShuttle;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
@DiscriminatorValue("CREW_MEMBER")
public class CrewMemberPassenger extends ShuttlePassenger {

    private String employeeId;
    private String crewMemberName;

    public CrewMemberPassenger() {
    }

    public CrewMemberPassenger(String employeeId, String crewMemberName) {
        super(UUID.randomUUID().toString());
        this.employeeId = employeeId;
        this.crewMemberName = crewMemberName;
    }

    @Override
    public String getPassengerId() {
        return employeeId;
    }

    @Override
    public String getPassengerName() {
        return crewMemberName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getCrewMemberName() {
        return crewMemberName;
    }
}
