package uspace.application.cruise.cabin.dtos;

import java.util.List;

public class CabinsAttributionDto {
    public List<CabinAttributionDto> cabins;

    public CabinsAttributionDto() {
        //
    }

    public CabinsAttributionDto(List<CabinAttributionDto> cabins) {
        this.cabins = cabins;
    }
}
