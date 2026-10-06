package uspace.application.cruise.cabin;

import jakarta.inject.Inject;
import uspace.application.cruise.cabin.dtos.CabinsAttributionDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.cabin.CabinAttribution;
import uspace.domain.cruise.cabin.CabinAttributionCriteria;
import uspace.domain.cruise.cabin.CabinAttributor;
import uspace.domain.cruise.cabin.CabinId;
import uspace.domain.cruise.cabin.CabinRegistry;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;
import java.util.Map;

public class CabinAttributionService {

    private final CruiseRepository cruiseRepository;
    private final CabinRegistry cabinRegistry;
    private final CabinAttributor cabinAttributor;
    private final CabinAttributionAssembler cabinAttributionAssembler;

    @Inject
    public CabinAttributionService(CruiseRepository cruiseRepository, CabinRegistry cabinRegistry,
                                   CabinAttributor cabinAttributor, CabinAttributionAssembler cabinAttributionAssembler) {
        this.cruiseRepository = cruiseRepository;
        this.cabinRegistry = cabinRegistry;
        this.cabinAttributor = cabinAttributor;
        this.cabinAttributionAssembler = cabinAttributionAssembler;
    }

    public CabinsAttributionDto getCabinAttributions(String cruiseId, CabinAttributionCriteria criteria) {
        Cruise cruise = findCruiseOrThrow(cruiseId);
        Map<CabinType, List<CabinId>> cabinIdsByCategory = cabinRegistry.findCabinIdsByCategory();
        List<CabinAttribution> attributions = cruise.generateAttributions(cabinAttributor, cabinIdsByCategory, criteria);
        return cabinAttributionAssembler.toDto(attributions);
    }

    private Cruise findCruiseOrThrow(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }
        return cruise;
    }
}