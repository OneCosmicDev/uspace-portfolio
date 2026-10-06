package uspace.domain.cruise.emergencyShuttle;

import java.util.ArrayList;
import java.util.List;

public class EmergencyShuttle {

    private final ShuttleType type;
    private final int capacity;
    private final List<ShuttlePassenger> passengers = new ArrayList<>();

    EmergencyShuttle(ShuttleType type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    void addPassenger(ShuttlePassenger passenger) {
        passengers.add(passenger);
    }

    boolean isFull() {
        return passengers.size() >= capacity;
    }

    public ShuttleType getType() {
        return type;
    }

    public List<TravelerPassenger> getTravelers() {
        return passengers.stream()
                .filter(TravelerPassenger.class::isInstance)
                .map(TravelerPassenger.class::cast)
                .toList();
    }

    public List<CrewMemberPassenger> getCrewMembers() {
        return passengers.stream()
                .filter(CrewMemberPassenger.class::isInstance)
                .map(CrewMemberPassenger.class::cast)
                .toList();
    }
}
