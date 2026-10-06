package uspace.domain.cruise.emergencyShuttle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.booking.traveler.AdultTraveler;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.crew.crewMember.CrewMemberName;
import uspace.domain.cruise.crew.crewMember.employeeId.EmployeeId;
import uspace.domain.cruise.oxygen.Oxygen;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyShuttleManifestTest {

    private static final int RESCUE_SHIP_COUNT = 3;
    private static final int RESCUE_SHIP_CAPACITY = 5;
    private static final int RESCUE_SHIP_COST = 10;
    private static final int STANDARD_SHUTTLE_CAPACITY = 3;
    private static final int STANDARD_SHUTTLE_COST = 5;

    private EmergencyShuttleManifest manifest;

    @BeforeEach
    void setUp() {
        manifest = new EmergencyShuttleManifest(
                new EmergencyShuttleManifestId(),
                RESCUE_SHIP_COUNT, RESCUE_SHIP_CAPACITY, RESCUE_SHIP_COST,
                STANDARD_SHUTTLE_CAPACITY, STANDARD_SHUTTLE_COST
        );
    }

    private AdultTraveler anyTraveler(String id, String name) {
        return new AdultTraveler(new TravelerId(id), new TravelerName(name), List.of(), new Oxygen(1), List.of());
    }

    private CrewMember anyCrewMember(String id, String name) {
        return new CrewMember(new EmployeeId(id), new CrewMemberName(name), new Oxygen(1));
    }

    @Test
    void givenNoPassengers_whenComputeShuttles_thenReturnsEmptyList() {
        assertTrue(manifest.computeShuttles().isEmpty());
    }

    @Test
    void givenNoPassengers_whenComputeTotalCost_thenReturnsZero() {
        assertEquals(0, manifest.computeTotalCost());
    }

    @Test
    void givenOneTraveler_whenComputeShuttles_thenOneRescueShipWithOneTraveler() {
        manifest.addTraveler(anyTraveler("id-1", "Alice"));

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(1, shuttles.size());
        assertEquals(ShuttleType.RESCUE_SHIP, shuttles.get(0).getType());
        assertEquals(1, shuttles.get(0).getTravelers().size());
        assertEquals("Alice", shuttles.get(0).getTravelers().get(0).getTravelerName());
    }

    @Test
    void givenOneCrewMember_whenComputeShuttles_thenOneRescueShipWithOneCrewMember() {
        manifest.addCrewMember(anyCrewMember("ABC123", "Bob"));

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(1, shuttles.size());
        assertEquals(ShuttleType.RESCUE_SHIP, shuttles.get(0).getType());
        assertEquals(1, shuttles.get(0).getCrewMembers().size());
        assertEquals("Bob", shuttles.get(0).getCrewMembers().get(0).getCrewMemberName());
    }

    @Test
    void givenRescueShipCapacityPassengers_whenComputeShuttles_thenOneFullRescueShip() {
        for (int i = 0; i < RESCUE_SHIP_CAPACITY; i++) {
            manifest.addTraveler(anyTraveler("id-" + i, "Traveler " + i));
        }

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(1, shuttles.size());
        assertEquals(ShuttleType.RESCUE_SHIP, shuttles.get(0).getType());
        assertEquals(RESCUE_SHIP_CAPACITY, shuttles.get(0).getTravelers().size());
    }

    @Test
    void givenRescueShipCapacityPlusOne_whenComputeShuttles_thenTwoRescueShips() {
        for (int i = 0; i <= RESCUE_SHIP_CAPACITY; i++) {
            manifest.addTraveler(anyTraveler("id-" + i, "Traveler " + i));
        }

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(2, shuttles.size());
        assertEquals(ShuttleType.RESCUE_SHIP, shuttles.get(0).getType());
        assertEquals(ShuttleType.RESCUE_SHIP, shuttles.get(1).getType());
        assertEquals(RESCUE_SHIP_CAPACITY, shuttles.get(0).getTravelers().size());
        assertEquals(1, shuttles.get(1).getTravelers().size());
    }

    @Test
    void givenAllRescueShipsFull_whenComputeShuttles_thenExactlyMaxRescueShips() {
        for (int i = 0; i < RESCUE_SHIP_COUNT * RESCUE_SHIP_CAPACITY; i++) {
            manifest.addTraveler(anyTraveler("id-" + i, "Traveler " + i));
        }

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(RESCUE_SHIP_COUNT, shuttles.size());
        assertTrue(shuttles.stream().allMatch(s -> s.getType() == ShuttleType.RESCUE_SHIP));
    }

    @Test
    void givenAllRescueShipsFullPlusOne_whenComputeShuttles_thenOneStandardShuttleOpened() {
        for (int i = 0; i < RESCUE_SHIP_COUNT * RESCUE_SHIP_CAPACITY + 1; i++) {
            manifest.addTraveler(anyTraveler("id-" + i, "Traveler " + i));
        }

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(RESCUE_SHIP_COUNT + 1, shuttles.size());
        assertEquals(ShuttleType.STANDARD_SHUTTLE, shuttles.get(RESCUE_SHIP_COUNT).getType());
        assertEquals(1, shuttles.get(RESCUE_SHIP_COUNT).getTravelers().size());
    }

    @Test
    void givenStandardShuttleOverflow_whenComputeShuttles_thenTwoStandardShuttlesOpened() {
        int totalPassengers = RESCUE_SHIP_COUNT * RESCUE_SHIP_CAPACITY + STANDARD_SHUTTLE_CAPACITY + 1;
        for (int i = 0; i < totalPassengers; i++) {
            manifest.addTraveler(anyTraveler("id-" + i, "Traveler " + i));
        }

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(RESCUE_SHIP_COUNT + 2, shuttles.size());
        assertEquals(ShuttleType.STANDARD_SHUTTLE, shuttles.get(RESCUE_SHIP_COUNT).getType());
        assertEquals(ShuttleType.STANDARD_SHUTTLE, shuttles.get(RESCUE_SHIP_COUNT + 1).getType());
        assertEquals(STANDARD_SHUTTLE_CAPACITY, shuttles.get(RESCUE_SHIP_COUNT).getTravelers().size());
        assertEquals(1, shuttles.get(RESCUE_SHIP_COUNT + 1).getTravelers().size());
    }

    @Test
    void givenMixedTravelersAndCrewMembers_whenComputeShuttles_thenBothInSameShuttle() {
        manifest.addTraveler(anyTraveler("id-1", "Alice"));
        manifest.addCrewMember(anyCrewMember("ABC123", "Bob"));
        manifest.addTraveler(anyTraveler("id-2", "Carol"));

        List<EmergencyShuttle> shuttles = manifest.computeShuttles();

        assertEquals(1, shuttles.size());
        assertEquals(2, shuttles.get(0).getTravelers().size());
        assertEquals(1, shuttles.get(0).getCrewMembers().size());
    }

    @Test
    void givenOneRescueShipUsed_whenComputeTotalCost_thenRescueShipCost() {
        manifest.addTraveler(anyTraveler("id-1", "Alice"));

        assertEquals(RESCUE_SHIP_COST, manifest.computeTotalCost());
    }

    @Test
    void givenAllRescueShipsFullPlusOnePassenger_whenComputeTotalCost_thenRescueAndStandardCost() {
        for (int i = 0; i < RESCUE_SHIP_COUNT * RESCUE_SHIP_CAPACITY + 1; i++) {
            manifest.addTraveler(anyTraveler("id-" + i, "Traveler " + i));
        }

        int expectedCost = RESCUE_SHIP_COUNT * RESCUE_SHIP_COST + STANDARD_SHUTTLE_COST;
        assertEquals(expectedCost, manifest.computeTotalCost());
    }
}
