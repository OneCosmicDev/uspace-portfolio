package uspace.domain.cruise.dateTime;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CruiseDateTimeTest {

    private static final LocalDateTime ANY_LOCAL_DATE_TIME = LocalDateTime.of(2002, 4, 5, 1, 1);
    private static final CruiseDateTime ANY_CRUISE_DATE_TIME = new CruiseDateTime(ANY_LOCAL_DATE_TIME);
    private static final LocalDateTime ANY_LATER_LOCAL_DATE_TIME = ANY_LOCAL_DATE_TIME.plusDays(2);
    private static final CruiseDateTime ANY_LATER_CRUISE_DATE_TIME = new CruiseDateTime(ANY_LATER_LOCAL_DATE_TIME);
    private static final int ANY_NUMBER_OF_DAYS = 2;

    @Test
    void givenLaterCruiseDateTime_whenCalculateDaysBefore_thenReturnDaysBeforeGivenDateTime() {
        CruiseDateTime laterCruiseDateTime = new CruiseDateTime(ANY_LOCAL_DATE_TIME.plusDays(ANY_NUMBER_OF_DAYS));

        int daysBeforeDateTime = ANY_CRUISE_DATE_TIME.calculateDaysBefore(laterCruiseDateTime);

        assertEquals(ANY_NUMBER_OF_DAYS, daysBeforeDateTime);
    }

    @Test
    void givenSameCruiseDateTime_whenCalculateDaysBefore_thenReturnZero() {
        int daysBeforeDateTime = ANY_CRUISE_DATE_TIME.calculateDaysBefore(ANY_CRUISE_DATE_TIME);

        assertEquals(0, daysBeforeDateTime);
    }

    @Test
    void givenEarlierCruiseDateTime_whenCalculateDaysBefore_thenReturnNegativeDaysBeforeGivenDateTime() {
        CruiseDateTime earlierCruiseDateTime = new CruiseDateTime(ANY_LOCAL_DATE_TIME.minusDays(ANY_NUMBER_OF_DAYS));

        int daysBeforeDateTime = ANY_CRUISE_DATE_TIME.calculateDaysBefore(earlierCruiseDateTime);

        assertEquals(-ANY_NUMBER_OF_DAYS, daysBeforeDateTime);
    }

    @Test
    void givenCruiseDateTimeBeforeTheFirstDateTime_whenValidateIfIsBetween_thenReturnFalse() {
        CruiseDateTime cruiseDateTimeBeforeTheFirstDateTime = new CruiseDateTime(ANY_LOCAL_DATE_TIME.minusDays(ANY_NUMBER_OF_DAYS));

        boolean isBetween = cruiseDateTimeBeforeTheFirstDateTime.isBetween(ANY_CRUISE_DATE_TIME, ANY_LATER_CRUISE_DATE_TIME);

        assertFalse(isBetween);
    }

    @Test
    void givenCruiseDateTimeEqualToTheFirstDateTime_whenValidateIfIsBetween_thenReturnTrue() {
        boolean isBetween = ANY_CRUISE_DATE_TIME.isBetween(ANY_CRUISE_DATE_TIME, ANY_LATER_CRUISE_DATE_TIME);

        assertTrue(isBetween);
    }

    @Test
    void givenCruiseDateTimeAfterTheLastDateTime_whenValidateIfIsBetween_thenReturnFalse() {
        CruiseDateTime cruiseDateTimeAfterTheLastDateTime = new CruiseDateTime(ANY_LATER_LOCAL_DATE_TIME.plusDays(ANY_NUMBER_OF_DAYS));

        boolean isBetween = cruiseDateTimeAfterTheLastDateTime.isBetween(ANY_CRUISE_DATE_TIME, ANY_LATER_CRUISE_DATE_TIME);

        assertFalse(isBetween);
    }

    @Test
    void givenCruiseDateTimeEqualToTheLastDateTime_whenValidateIfIsBetween_thenReturnTrue() {
        boolean isBetween = ANY_LATER_CRUISE_DATE_TIME.isBetween(ANY_CRUISE_DATE_TIME, ANY_LATER_CRUISE_DATE_TIME);

        assertTrue(isBetween);
    }

    @Test
    void givenCruiseDateTimeAfterTheFirstAndBeforeTheLastDateTime_whenValidateIfIsBetween_thenReturnTrue() {
        CruiseDateTime CruiseDateTimeAfterTheFirstAndBeforeTheLastDateTime = new CruiseDateTime(LocalDateTime.of(2002, 4, 6, 2, 1));

        boolean isBetween = CruiseDateTimeAfterTheFirstAndBeforeTheLastDateTime.isBetween(ANY_CRUISE_DATE_TIME, ANY_LATER_CRUISE_DATE_TIME);

        assertTrue(isBetween);
    }
}