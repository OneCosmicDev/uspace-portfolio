package uspace.domain.cruise.hyperdrive.module.maintenance.exceptions;

public class InvalidHyperdriveMaintenanceDurationException extends RuntimeException {

    public InvalidHyperdriveMaintenanceDurationException() {
        super("Maintenance must have a duration greater or equal to the module power level");
    }
}
