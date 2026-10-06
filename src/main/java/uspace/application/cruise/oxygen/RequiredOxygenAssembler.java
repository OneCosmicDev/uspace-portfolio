package uspace.application.cruise.oxygen;

import uspace.application.cruise.oxygen.dtos.RequiredOxygenDto;
import uspace.domain.cruise.oxygen.Oxygen;

public class RequiredOxygenAssembler {
    public RequiredOxygenDto toDto(Oxygen oxygen) {
        int roundedOxygenUnits = (int) Math.ceil(oxygen.toDouble());
        return new RequiredOxygenDto(roundedOxygenUnits);
    }
}
