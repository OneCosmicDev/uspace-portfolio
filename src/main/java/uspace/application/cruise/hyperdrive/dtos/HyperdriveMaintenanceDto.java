package uspace.application.cruise.hyperdrive.dtos;

import java.util.List;

public class HyperdriveMaintenanceDto {
    public int duration;
    public List<String> employeeIds;

    public HyperdriveMaintenanceDto() {}

    public HyperdriveMaintenanceDto(int duration, List<String> employeeIds) {
        this.duration = duration;
        this.employeeIds = employeeIds;
    }
}
