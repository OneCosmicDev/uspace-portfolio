package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.oxygen.Oxygen;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TravelerFactoryTest {

    private static final String ANY_NAME = "Bob";
    private static final Oxygen ANY_OXYGEN_CONSUMPTION_PER_MINUTE = new Oxygen(100);
    private static final Map<TravelerCategory, Oxygen> ANY_TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTE = Map.of(
            TravelerCategory.ADULT, ANY_OXYGEN_CONSUMPTION_PER_MINUTE,
            TravelerCategory.CHILD, ANY_OXYGEN_CONSUMPTION_PER_MINUTE,
            TravelerCategory.SENIOR, ANY_OXYGEN_CONSUMPTION_PER_MINUTE);
    private TravelerFactory travelerFactory;

    @BeforeEach
    void createTravelerFactory() {
        travelerFactory = new TravelerFactory(ANY_TRAVELERS_OXYGEN_CONSUMPTION_PER_MINUTE);
    }

    @Test
    void givenAdultCategory_whenCreate_thenCreateAdultTraveler() {
        TravelerCategory adultCategory = TravelerCategory.ADULT;

        Traveler traveler = travelerFactory.create(ANY_NAME, adultCategory);

        assertTrue(traveler instanceof AdultTraveler);
    }

    @Test
    void givenChildCategory_whenCreate_thenCreateChildTraveler() {
        TravelerCategory childCategory = TravelerCategory.CHILD;

        Traveler traveler = travelerFactory.create(ANY_NAME, childCategory);

        assertTrue(traveler instanceof ChildTraveler);
    }

    @Test
    void givenSeniorCategory_whenCreate_thenCreateSeniorTraveler() {
        TravelerCategory seniorCategory = TravelerCategory.SENIOR;

        Traveler traveler = travelerFactory.create(ANY_NAME, seniorCategory);

        assertTrue(traveler instanceof SeniorTraveler);
    }
}