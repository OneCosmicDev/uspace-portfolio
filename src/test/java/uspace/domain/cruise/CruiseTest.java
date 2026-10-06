package uspace.domain.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.crew.Crew;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.emergencyShuttle.EmergencyShuttleManifest;
import uspace.domain.cruise.hyperdrive.HyperdriveInventory;
import uspace.domain.cruise.hyperdrive.module.HyperdriveModule;
import uspace.domain.cruise.hyperdrive.module.exceptions.InvalidHyperdriveModuleActivationDateException;
import uspace.domain.cruise.hyperdrive.module.maintenance.HyperdriveMaintenance;
import uspace.domain.cruise.hyperdrive.module.maintenance.exceptions.EmployeeNotFoundException;
import uspace.domain.cruise.hyperdrive.module.id.HyperdriveModuleId;
import uspace.domain.cruise.oxygen.Oxygen;
import uspace.domain.cruise.oxygen.OxygenCalculator;
import uspace.domain.cruise.oxygen.OxygenCalculatorFactory;
import uspace.domain.cruise.oxygen.exceptions.OxygenCalculationDateOutsideCruiseException;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CruiseTest {

    private static final CruiseId ANY_CRUISE_ID = new CruiseId("ANY_CRUISE_ID");
    private static final LocalDateTime ANY_LOCAL_DEPARTURE_DATE_TIME = LocalDateTime.of(2222, 3, 4, 1, 2);
    private static final CruiseDateTime ANY_DEPARTURE_DATE_TIME = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME);
    private static final CruiseDateTime ANY_END_DATE_TIME = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusDays(1));
    private static final CruiseDateTime ANY_DATE_TIME_BEFORE_DEPARTURE = new CruiseDateTime(
            ANY_LOCAL_DEPARTURE_DATE_TIME.minusHours(1));
    private static final CruiseDateTime ANY_DATE_TIME_AFTER_DEPARTURE = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusHours(1));
    private static final CruiseDateTime ANY_DATE_TIME_DURING_CRUISE = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusHours(1));
    private static final CruiseDateTime ANY_DATE_TIME_AFTER_END = new CruiseDateTime(ANY_LOCAL_DEPARTURE_DATE_TIME.plusDays(2));
    private static final BookingId ANY_BOOKING_ID = new BookingId("ANY_BOOKING_ID");
    private static final TravelerId ANY_TRAVELER_ID = new TravelerId();
    private static final CrewMember ANY_CREW_MEMBER = new CrewMember();
    private static final HyperdriveModuleId ANY_HYPERDRIVE_MODULE_ID = new HyperdriveModuleId("anyId");
    private static final Oxygen ANY_OXYGEN = new Oxygen(2222);
    private static final Sport ANY_SPORT = new Sport();
    private Cruise cruise;
    @Mock
    private CruiseBookings cruiseBookingsMock;
    @Mock
    private CabinAvailabilities cabinAvailabilitiesMock;
    @Mock
    private ZeroGravityExperience zeroGravityExperienceMock;
    @Mock
    private Booking bookingMock;
    @Mock
    private HyperdriveInventory hyperdriveInventoryMock;
    @Mock
    private HyperdriveModule hyperdriveModuleMock;
    @Mock
    private HyperdriveMaintenance hyperdriveMaintenanceMock;
    @Mock
    private Crew crewMock;
    @Mock
    private OxygenCalculatorFactory oxygenCalculatorFactoryMock;
    @Mock
    private OxygenCalculator oxygenCalculatorMock;
    @Mock
    private EmergencyShuttleManifest emergencyShuttleManifestMock;
    @Mock
    private Traveler travelerMock;

    @BeforeEach
    void createCruise() {
        cruise = new Cruise(ANY_CRUISE_ID, ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME,
                            cabinAvailabilitiesMock, cruiseBookingsMock, zeroGravityExperienceMock,
                            hyperdriveInventoryMock, crewMock, oxygenCalculatorFactoryMock,
                            emergencyShuttleManifestMock);
    }

    @Test
    void givenExperienceBookingDateTimeAfterDeparture_whenBookZeroGravityExperience_thenThrowZeroGravityExperienceBookingTimeException() {
        Executable bookZeroGravityExperience = () -> cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID,
                                                                                      ANY_DATE_TIME_AFTER_DEPARTURE);

        assertThrows(ZeroGravityExperienceBookingTimeException.class, bookZeroGravityExperience);
    }

    @Test
    void givenExperienceBookingDateTimeBeforeDepartureAndNonExistingBookingId_whenBookZeroGravityExperience_thenThrowTravelerNotFoundException() {
        Executable bookZeroGravityExperience = () -> cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID,
                                                                                      ANY_DATE_TIME_BEFORE_DEPARTURE);

        assertThrows(TravelerNotFoundException.class, bookZeroGravityExperience);
    }

    @Test
    void givenExperienceBookingDateTimeBeforeDepartureAndExistingBookingId_whenBookZeroGravityExperience_thenBookingBookZeroGravityExperience() {
        when(cruiseBookingsMock.findById(ANY_BOOKING_ID)).thenReturn(bookingMock);

        cruise.bookZeroGravityExperience(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_DATE_TIME_BEFORE_DEPARTURE);

        verify(bookingMock).bookZeroGravityExperience(ANY_TRAVELER_ID, zeroGravityExperienceMock);
    }

    @Test
    void givenHyperdriveModuleNotActivatedDuringCruiseTime_whenAddHyperdriveModule_thenThrowInvalidHyperdriveModuleActivationDateException() {
        when(hyperdriveModuleMock.isActivationBetween(ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME)).thenReturn(false);

        Executable addHyperdriveModule = () -> cruise.addHyperdriveModule(hyperdriveModuleMock);

        assertThrows(InvalidHyperdriveModuleActivationDateException.class, addHyperdriveModule);
    }

    @Test
    void givenHyperdriveModuleActivatedDuringCruiseTime_whenAddHyperdriveModule_thenAddToInventory() {
        when(hyperdriveModuleMock.isActivationBetween(ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME)).thenReturn(true);

        cruise.addHyperdriveModule(hyperdriveModuleMock);

        verify(hyperdriveInventoryMock).addHyperdriveModule(hyperdriveModuleMock);
    }

    @Test
    void whenAddCrewMember_thenCrewAddCrewMemberBasedOnCabinAvailabilities() {
        cruise.addCrewMember(ANY_CREW_MEMBER);

        verify(crewMock).addCrewMember(ANY_CREW_MEMBER, cabinAvailabilitiesMock);
    }

    @Test
    void givenMaintenanceWithInvalidEmployees_whenAddHyperdriveMaintenance_thenThrowEmployeeNotFoundException() {
        when(hyperdriveMaintenanceMock.hasValidEmployees(crewMock)).thenReturn(false);

        Executable addHyperdriveMaintenance = () -> cruise.addHyperdriveMaintenance(ANY_HYPERDRIVE_MODULE_ID, hyperdriveMaintenanceMock);

        assertThrows(EmployeeNotFoundException.class, addHyperdriveMaintenance);
    }

    @Test
    void givenMaintenanceWithValidEmployees_whenAddHyperdriveMaintenance_thenAddMaintenanceToHyperdriveInventory() {
        when(hyperdriveMaintenanceMock.hasValidEmployees(crewMock)).thenReturn(true);

        cruise.addHyperdriveMaintenance(ANY_HYPERDRIVE_MODULE_ID, hyperdriveMaintenanceMock);

        verify(hyperdriveInventoryMock).addMaintenance(ANY_HYPERDRIVE_MODULE_ID, hyperdriveMaintenanceMock);
    }

    @Test
    void givenCalculationDateTimeBeforeCruiseTime_whenCalculateRequiredOxygen_thenThrowOxygenCalculationDateOutsideCruiseException(){
        Executable calculateRequiredOxygen = () -> cruise.calculateRequiredOxygen(ANY_DATE_TIME_BEFORE_DEPARTURE);

        assertThrows(OxygenCalculationDateOutsideCruiseException.class, calculateRequiredOxygen);
    }

    @Test
    void givenCalculationDateTimeAfterCruiseTime_whenCalculateRequiredOxygen_thenThrowOxygenCalculationDateOutsideCruiseException(){
        Executable calculateRequiredOxygen = () -> cruise.calculateRequiredOxygen(ANY_DATE_TIME_AFTER_END);

        assertThrows(OxygenCalculationDateOutsideCruiseException.class, calculateRequiredOxygen);
    }

    @Test
    void givenCalculationDateTimeDuringCruiseTime_whenCalculateRequiredOxygen_thenReturnCalculatedRequiredOxygen(){
        when(oxygenCalculatorFactoryMock.create(ANY_DATE_TIME_DURING_CRUISE, ANY_DEPARTURE_DATE_TIME, ANY_END_DATE_TIME)).thenReturn(oxygenCalculatorMock);
        when(oxygenCalculatorMock.calculateRequiredOxygen(cruiseBookingsMock, crewMock)).thenReturn(ANY_OXYGEN);

        Oxygen requiredOxygen = cruise.calculateRequiredOxygen(ANY_DATE_TIME_DURING_CRUISE);

        assertEquals(ANY_OXYGEN, requiredOxygen);
    }

    @Test
    void givenNonExistingBookingId_whenBookSport_thenThrowTravelerNotFoundException() {
        Executable bookSport = () -> cruise.bookSport(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT);

        assertThrows(TravelerNotFoundException.class, bookSport);
    }

    @Test
    void givenExistingBookingId_whenBookSport_thenBookingBookSport() {
        when(cruiseBookingsMock.findById(ANY_BOOKING_ID)).thenReturn(bookingMock);

        cruise.bookSport(ANY_BOOKING_ID, ANY_TRAVELER_ID, ANY_SPORT);

        verify(bookingMock).bookSport(ANY_TRAVELER_ID, ANY_SPORT);
    }

    @Test
    void whenProcessBooking_thenEmergencyShuttleManifestReceivesTravelers() {
        when(bookingMock.getTravelers()).thenReturn(List.of(travelerMock));

        cruise.processBooking(bookingMock);

        verify(emergencyShuttleManifestMock).addTraveler(travelerMock);
    }

    @Test
    void whenAddCrewMember_thenEmergencyShuttleManifestReceivesCrewMember() {
        cruise.addCrewMember(ANY_CREW_MEMBER);

        verify(emergencyShuttleManifestMock).addCrewMember(ANY_CREW_MEMBER);
    }
}