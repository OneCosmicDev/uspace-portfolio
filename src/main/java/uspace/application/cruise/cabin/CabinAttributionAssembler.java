package uspace.application.cruise.cabin;

import uspace.application.cruise.cabin.dtos.CabinAttributionDto;
import uspace.application.cruise.cabin.dtos.CabinsAttributionDto;
import uspace.domain.cruise.cabin.CabinAttribution;

import java.util.List;

public class CabinAttributionAssembler {

    public CabinsAttributionDto toDto(List<CabinAttribution> attributions) {
        List<CabinAttributionDto> cabinAttributionDtos = attributions.stream()
                .map(attribution -> new CabinAttributionDto(
                        attribution.getCabinId().toString(),
                        attribution.getBookingId().toString(),
                        attribution.getCabinType().toString()))
                .toList();

        return new CabinsAttributionDto(cabinAttributionDtos);
    }
}