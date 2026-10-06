package uspace.application.cruise.cabin.dtos;

public class CabinAttributionDto {
    public String cabinId;
    public String bookingId;
    public String category;

    public CabinAttributionDto() {
        //
    }

    public CabinAttributionDto(String cabinId, String bookingId, String category) {
        this.cabinId = cabinId;
        this.bookingId = bookingId;
        this.category = category;
    }
}
