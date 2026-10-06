package uspace.application.cruise;

import jakarta.inject.Inject;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.oxygen.RequiredOxygenAssembler;
import uspace.application.cruise.oxygen.dtos.RequiredOxygenDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.oxygen.Oxygen;

import java.time.LocalDateTime;

public class CruiseService {

    private final CruiseRepository cruiseRepository;
    private final CruiseAssembler cruiseAssembler;
    private final LocalDateTimeParser localDateTimeParser;
    private final  RequiredOxygenAssembler requiredOxygenAssembler;

    @Inject
    public CruiseService(CruiseRepository cruiseRepository, CruiseAssembler cruiseAssembler,
                         LocalDateTimeParser localDateTimeParser, RequiredOxygenAssembler requiredOxygenAssembler) {
        this.cruiseRepository = cruiseRepository;
        this.cruiseAssembler = cruiseAssembler;
        this.localDateTimeParser = localDateTimeParser;
        this.requiredOxygenAssembler = requiredOxygenAssembler;
    }

    public CruiseDto findCruise(String cruiseId) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        return cruiseAssembler.toDto(cruise);
    }

    public RequiredOxygenDto getRequiredOxygen(String cruiseId, String calculationDateTime) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        LocalDateTime calculationLocalDateTime = localDateTimeParser.parse(calculationDateTime);
        CruiseDateTime calculationCruiseDateTime = new CruiseDateTime(calculationLocalDateTime);
        Oxygen oxygen = cruise.calculateRequiredOxygen(calculationCruiseDateTime);

        return requiredOxygenAssembler.toDto(oxygen);
    }
}
