package uspace.application.cruise.emergencyShuttle.dtos;

public class CrewMemberPassengerDto {
    public String employeeId;
    public String crewMemberName;

    public CrewMemberPassengerDto() {
        //
    }

    public CrewMemberPassengerDto(String employeeId, String crewMemberName) {
        this.employeeId = employeeId;
        this.crewMemberName = crewMemberName;
    }
}
