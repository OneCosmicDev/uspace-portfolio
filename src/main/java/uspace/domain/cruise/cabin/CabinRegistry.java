package uspace.domain.cruise.cabin;

import java.util.List;
import java.util.Map;

public interface CabinRegistry {
    Map<CabinType, List<CabinId>> findCabinIdsByCategory();
}
