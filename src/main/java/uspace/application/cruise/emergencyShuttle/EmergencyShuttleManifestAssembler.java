package uspace.application.cruise.emergencyShuttle;

import uspace.application.cruise.emergencyShuttle.dtos.CrewMemberPassengerDto;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttleDto;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttlesManifestDto;
import uspace.application.cruise.emergencyShuttle.dtos.TravelerPassengerDto;
import uspace.domain.cruise.emergencyShuttle.CrewMemberPassenger;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttle;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttleManifest;
import uspace.domain.cruise.emergencyShuttle.TravelerPassenger;

import java.util.List;

public class EmergencyShuttleManifestAssembler {

    public EmergencyShuttlesManifestDto toDto(EmergencyShuttleManifest manifest) {
        List<EmergencyShuttle> shuttles = manifest.computeShuttles();
        List<EmergencyShuttleDto> shuttleDtos = shuttles.stream()
                .map(this::toDto)
                .toList();
        return new EmergencyShuttlesManifestDto(manifest.computeTotalCost(shuttles), shuttleDtos);
    }

    private EmergencyShuttleDto toDto(EmergencyShuttle shuttle) {
        List<TravelerPassengerDto> travelerDtos = shuttle.getTravelers().stream()
                .map(this::toDto)
                .toList();
        List<CrewMemberPassengerDto> crewMemberDtos = shuttle.getCrewMembers().stream()
                .map(this::toDto)
                .toList();
        return new EmergencyShuttleDto(shuttle.getType().toString(), travelerDtos, crewMemberDtos);
    }

    private TravelerPassengerDto toDto(TravelerPassenger traveler) {
        return new TravelerPassengerDto(traveler.getTravelerId(), traveler.getTravelerName());
    }

    private CrewMemberPassengerDto toDto(CrewMemberPassenger crewMember) {
        return new CrewMemberPassengerDto(crewMember.getEmployeeId(), crewMember.getCrewMemberName());
    }
}
