package uspace.application.cruise.oxygen;

import org.junit.jupiter.api.Test;
import uspace.application.cruise.oxygen.dtos.RequiredOxygenDto;
import uspace.domain.cruise.oxygen.Oxygen;

import static org.junit.jupiter.api.Assertions.*;

class RequiredOxygenAssemblerTest {

    @Test
    void whenConvertToDto_thenRoundRequiredOxygenToUpperInteger() {
        RequiredOxygenAssembler requiredOxygenAssembler = new RequiredOxygenAssembler();
        Oxygen anyOxygen = new Oxygen(1000.2);
        int expectedRoundedRequiredOxygen = 1001;

        RequiredOxygenDto requiredOxygenDto = requiredOxygenAssembler.toDto(anyOxygen);

        assertEquals(expectedRoundedRequiredOxygen, requiredOxygenDto.requiredOxygen);
    }

}