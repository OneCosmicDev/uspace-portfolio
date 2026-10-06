package uspace.application.cruise.emergencyShuttle;

import jakarta.inject.Inject;
import uspace.application.cruise.emergencyShuttle.dtos.EmergencyShuttlesManifestDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

public class EmergencyShuttleService {

    private final CruiseRepository cruiseRepository;
    private final EmergencyShuttleManifestAssembler emergencyShuttleManifestAssembler;

    @Inject
    public EmergencyShuttleService(CruiseRepository cruiseRepository,
                                   EmergencyShuttleManifestAssembler emergencyShuttleManifestAssembler) {
        this.cruiseRepository = cruiseRepository;
        this.emergencyShuttleManifestAssembler = emergencyShuttleManifestAssembler;
    }

    public EmergencyShuttlesManifestDto getEmergencyShuttleManifest(String cruiseId) {
        Cruise cruise = findCruiseOrThrow(cruiseId);
        return emergencyShuttleManifestAssembler.toDto(cruise.getEmergencyShuttleManifest());
    }

    private Cruise findCruiseOrThrow(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }
        return cruise;
    }
}
