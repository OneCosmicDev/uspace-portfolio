package uspace.infra.cabin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.domain.cruise.cabin.CabinId;
import uspace.domain.cruise.cabin.CabinRegistry;
import uspace.domain.cruise.cabin.CabinType;
import uspace.infra.cabin.dtos.CabinCategoryDto;
import uspace.infra.cabin.dtos.CabinFileDto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CabinJsonReader implements CabinRegistry {
    private final Logger logger = LoggerFactory.getLogger(CabinJsonReader.class);
    private final String cabinJsonPath;

    public CabinJsonReader(String cabinJsonPath) {
        this.cabinJsonPath = cabinJsonPath;
    }

    @Override
    public Map<CabinType, List<CabinId>> findCabinIdsByCategory() {
        List<CabinCategoryDto> cabinCategories = readCabinJson();

        Map<CabinType, List<CabinId>> cabinIdsByCategory = new LinkedHashMap<>();
        for (CabinCategoryDto cabinCategoryDto : cabinCategories) {
            CabinType cabinType = CabinType.fromString(cabinCategoryDto.category);
            List<CabinId> cabinIds = cabinCategoryDto.ids.stream().map(CabinId::new).toList();
            cabinIdsByCategory.put(cabinType, cabinIds);
        }

        return cabinIdsByCategory;
    }

    private List<CabinCategoryDto> readCabinJson() {
        String cabinJsonStr = null;
        try {
            FileInputStream stream = new FileInputStream(cabinJsonPath);
            cabinJsonStr = new String(stream.readAllBytes());
        } catch (IOException e) {
            logger.error("Error while trying to read cabin json file " + cabinJsonPath, e);
            throw new RuntimeException(e);
        }

        try {
            CabinFileDto cabinFileDto = new ObjectMapper().readValue(cabinJsonStr, CabinFileDto.class);
            return cabinFileDto.cabins;
        } catch (JsonProcessingException e) {
            logger.error("Error while trying to parse cabin json file " + cabinJsonPath, e);
            throw new RuntimeException(e);
        }
    }
}