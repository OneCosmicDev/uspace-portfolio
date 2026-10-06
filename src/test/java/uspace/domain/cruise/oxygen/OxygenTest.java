package uspace.domain.cruise.oxygen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OxygenTest {

    private Oxygen oxygen = new Oxygen(1500);

    @Test
    void givenZero_whenAddToOxygen_thenReturnOriginalOxygenUnits(){
        Oxygen zero = new Oxygen(0);

        Oxygen sum = oxygen.add(zero);

        assertEquals(oxygen, sum);
    }

    @Test
    void whenAddAnotherOxygen_thenReturnTheSum(){
        Oxygen anyOtherOxygen = new Oxygen(150);
        Oxygen expectedSum = new Oxygen(1650);

        Oxygen sum = oxygen.add(anyOtherOxygen);

        assertEquals(expectedSum, sum);
    }

    @Test
    void whenMultiplyByAnyFactor_thenReturnOxygenValueMultiplied(){
        double anyFactor = 2;
        Oxygen expectedMultipliedOxygen = new Oxygen(3000);

        Oxygen multipliedOxygen = oxygen.multiply(anyFactor);

        assertEquals(expectedMultipliedOxygen, multipliedOxygen);
    }

}