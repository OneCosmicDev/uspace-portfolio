package uspace.api.cruise.hyperdrive;

import uspace.application.cruise.hyperdrive.dtos.HyperdriveMaintenanceDto;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

public class HyperdriveMaintenanceValidator {
    public void validate(HyperdriveMaintenanceDto hyperdriveMaintenanceDto) {
        if (hyperdriveMaintenanceDto.duration == 0) {
            throw new MissingParameterException("duration");
        }
        if (hyperdriveMaintenanceDto.duration < 0) {
            throw new InvalidParameterException("Maintenance duration must be greater than 0");
        }
        if (hyperdriveMaintenanceDto.employeeIds == null) {
            throw new MissingParameterException("employeeIds");
        }
    }
}
