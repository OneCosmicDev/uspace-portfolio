package uspace.application.cruise.emergencyShuttle.dtos;

public class TravelerPassengerDto {
    public String travelerId;
    public String travelerName;

    public TravelerPassengerDto() {
        //
    }

    public TravelerPassengerDto(String travelerId, String travelerName) {
        this.travelerId = travelerId;
        this.travelerName = travelerName;
    }
}
