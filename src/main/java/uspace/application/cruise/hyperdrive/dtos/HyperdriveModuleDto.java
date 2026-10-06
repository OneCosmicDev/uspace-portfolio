package uspace.application.cruise.hyperdrive.dtos;

import java.util.List;

public class HyperdriveModuleDto {

    public String id;
    public int powerLevel;
    public String activationDateTime;
    public String deactivationDateTime;
    public List<HyperdriveMaintenanceDto> maintenances;


    public HyperdriveModuleDto(String id, int powerLevel, String activationDateTime,
                               String deactivationDateTime, List<HyperdriveMaintenanceDto> maintenances) {
        this.id = id;
        this.powerLevel = powerLevel;
        this.activationDateTime = activationDateTime;
        this.deactivationDateTime = deactivationDateTime;
        this.maintenances = maintenances;
    }
}
