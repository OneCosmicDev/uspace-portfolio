package uspace.domain.cruise.emergencyShuttle;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "passenger_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ShuttlePassenger {

    @Id
    private String id;

    protected ShuttlePassenger() {
    }

    protected ShuttlePassenger(String id) {
        this.id = id;
    }

    public abstract String getPassengerId();

    public abstract String getPassengerName();
}
