package uspace.application.cruise.emergencyShuttle.dtos;

import java.util.List;

public class EmergencyShuttlesManifestDto {
    public int cost;
    public List<EmergencyShuttleDto> emergencyShuttles;

    public EmergencyShuttlesManifestDto() {
        //
    }

    public EmergencyShuttlesManifestDto(int cost, List<EmergencyShuttleDto> emergencyShuttles) {
        this.cost = cost;
        this.emergencyShuttles = emergencyShuttles;
    }
}
