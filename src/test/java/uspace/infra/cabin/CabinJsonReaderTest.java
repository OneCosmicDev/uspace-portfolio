package uspace.infra.cabin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.cabin.CabinId;
import uspace.domain.cruise.cabin.CabinType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CabinJsonReaderTest {
    private static final String CABINS_JSON = "src/test/java/uspace/infra/cabin/dataTest/cabins.json";
    private CabinJsonReader cabinJsonReader;

    @BeforeEach
    void createCabinJsonReader() {
        cabinJsonReader = new CabinJsonReader(CABINS_JSON);
    }

    @Test
    void givenCabinsJson_whenFindCabinIdsByCategory_thenReturnCabinIdsGroupedByCategory() {
        Map<CabinType, List<CabinId>> cabinIdsByCategory = cabinJsonReader.findCabinIdsByCategory();

        assertEquals(3, cabinIdsByCategory.size());
        assertEquals(List.of(new CabinId("A1"), new CabinId("A2"), new CabinId("A3")), cabinIdsByCategory.get(CabinType.STANDARD));
        assertEquals(List.of(new CabinId("K1"), new CabinId("K2")), cabinIdsByCategory.get(CabinType.DELUXE));
        assertEquals(List.of(new CabinId("R1")), cabinIdsByCategory.get(CabinType.SUITE));
    }

    @Test
    void givenCabinsJson_whenFindCabinIdsByCategory_thenCabinIdsAreInOrder() {
        Map<CabinType, List<CabinId>> cabinIdsByCategory = cabinJsonReader.findCabinIdsByCategory();

        List<CabinId> standardCabins = cabinIdsByCategory.get(CabinType.STANDARD);
        assertEquals(new CabinId("A1"), standardCabins.get(0));
        assertEquals(new CabinId("A2"), standardCabins.get(1));
        assertEquals(new CabinId("A3"), standardCabins.get(2));
    }
}
