package uspace.domain.cruise.hyperdrive.module.maintenance;

import jakarta.persistence.Embeddable;

@Embeddable
public class HyperdriveMaintenanceDuration {

    private int duration;

    public HyperdriveMaintenanceDuration() {}

    public HyperdriveMaintenanceDuration(int duration) {
        this.duration = duration;
    }

    public int toInt() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        HyperdriveMaintenanceDuration other = (HyperdriveMaintenanceDuration) obj;
        return this.duration == other.duration;
    }

    public boolean isGreaterOrEqualThan(HyperdriveMaintenanceDuration otherDuration) {
        return duration >= otherDuration.duration;
    }
}
