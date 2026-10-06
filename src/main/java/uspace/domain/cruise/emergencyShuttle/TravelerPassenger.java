package uspace.domain.cruise.emergencyShuttle;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
@DiscriminatorValue("TRAVELER")
public class TravelerPassenger extends ShuttlePassenger {

    private String travelerId;
    private String travelerName;

    public TravelerPassenger() {
    }

    public TravelerPassenger(String travelerId, String travelerName) {
        super(UUID.randomUUID().toString());
        this.travelerId = travelerId;
        this.travelerName = travelerName;
    }

    @Override
    public String getPassengerId() {
        return travelerId;
    }

    @Override
    public String getPassengerName() {
        return travelerName;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public String getTravelerName() {
        return travelerName;
    }
}
