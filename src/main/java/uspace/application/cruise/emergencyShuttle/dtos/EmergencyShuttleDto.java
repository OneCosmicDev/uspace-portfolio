package uspace.application.cruise.emergencyShuttle.dtos;

import java.util.List;

public class EmergencyShuttleDto {
    public String type;
    public List<TravelerPassengerDto> travelers;
    public List<CrewMemberPassengerDto> crewMembers;

    public EmergencyShuttleDto() {
        //
    }

    public EmergencyShuttleDto(String type, List<TravelerPassengerDto> travelers, List<CrewMemberPassengerDto> crewMembers) {
        this.type = type;
        this.travelers = travelers;
        this.crewMembers = crewMembers;
    }
}
