package uspace.domain.cruise.oxygen;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Oxygen {
    private BigDecimal oxygenInUnits;

    public Oxygen() {}

    public Oxygen(double oxygenInUnits) {
        this.oxygenInUnits = BigDecimal.valueOf(oxygenInUnits);
    }
    public Oxygen(BigDecimal oxygenInUnits) {
        this.oxygenInUnits = oxygenInUnits;
    }

    public double toDouble() {
        return oxygenInUnits.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Oxygen other = (Oxygen) obj;
        return this.oxygenInUnits.compareTo(other.oxygenInUnits) == 0;
    }

    public Oxygen add(Oxygen otherOxygen) {
        return new Oxygen(oxygenInUnits.add(otherOxygen.oxygenInUnits));
    }

    public Oxygen multiply(double value) {
        return new Oxygen(oxygenInUnits.multiply(BigDecimal.valueOf(value)));
    }
}
