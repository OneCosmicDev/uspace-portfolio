package uspace.domain.cruise.emergencyShuttle;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.crew.crewMember.CrewMember;

import java.util.ArrayList;
import java.util.List;

@Entity
public class EmergencyShuttleManifest {

    @Id
    private EmergencyShuttleManifestId id;

    private int rescueShipCount;
    private int rescueShipCapacity;
    private int rescueShipCost;
    private int standardShuttleCapacity;
    private int standardShuttleCost;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "manifest_id")
    @OrderColumn(name = "passenger_order")
    private List<ShuttlePassenger> passengers;

    public EmergencyShuttleManifest() {
    }

    public EmergencyShuttleManifest(EmergencyShuttleManifestId id, int rescueShipCount, int rescueShipCapacity,
                                    int rescueShipCost, int standardShuttleCapacity, int standardShuttleCost) {
        this.id = id;
        this.rescueShipCount = rescueShipCount;
        this.rescueShipCapacity = rescueShipCapacity;
        this.rescueShipCost = rescueShipCost;
        this.standardShuttleCapacity = standardShuttleCapacity;
        this.standardShuttleCost = standardShuttleCost;
        this.passengers = new ArrayList<>();
    }

    public void addTraveler(Traveler traveler) {
        passengers.add(new TravelerPassenger(traveler.getId().toString(), traveler.getName().toString()));
    }

    public void addCrewMember(CrewMember crewMember) {
        passengers.add(new CrewMemberPassenger(crewMember.getEmployeeId().toString(), crewMember.getName().toString()));
    }

    public List<EmergencyShuttle> computeShuttles() {
        List<EmergencyShuttle> result = new ArrayList<>();
        for (ShuttlePassenger passenger : passengers) {
            findAvailableShuttle(result).addPassenger(passenger);
        }
        return List.copyOf(result);
    }

    public int computeTotalCost() {
        return computeTotalCost(computeShuttles());
    }

    public int computeTotalCost(List<EmergencyShuttle> shuttles) {
        return shuttles.stream()
                .mapToInt(s -> s.getType() == ShuttleType.RESCUE_SHIP ? rescueShipCost : standardShuttleCost)
                .sum();
    }

    private EmergencyShuttle findAvailableShuttle(List<EmergencyShuttle> shuttles) {
        if (!shuttles.isEmpty() && !shuttles.get(shuttles.size() - 1).isFull()) {
            return shuttles.get(shuttles.size() - 1);
        }
        EmergencyShuttle newShuttle = openNextShuttle(shuttles);
        shuttles.add(newShuttle);
        return newShuttle;
    }

    private EmergencyShuttle openNextShuttle(List<EmergencyShuttle> shuttles) {
        long usedRescueShips = shuttles.stream().filter(s -> s.getType() == ShuttleType.RESCUE_SHIP).count();
        if (usedRescueShips < rescueShipCount) {
            return new EmergencyShuttle(ShuttleType.RESCUE_SHIP, rescueShipCapacity);
        }
        return new EmergencyShuttle(ShuttleType.STANDARD_SHUTTLE, standardShuttleCapacity);
    }
}
