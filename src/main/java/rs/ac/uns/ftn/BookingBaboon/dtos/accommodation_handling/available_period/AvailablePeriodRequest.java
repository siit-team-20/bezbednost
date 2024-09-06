package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

@Data
public class AvailablePeriodRequest {
    private Long id;
    private TimeSlot timeSlot;
    private Float PricePerNight;

    public AvailablePeriodRequest(long id, TimeSlot timeSlot, float pricePerNight) {
        this.id = id;
        this.timeSlot = timeSlot;
        this.PricePerNight = pricePerNight;
    }
}
