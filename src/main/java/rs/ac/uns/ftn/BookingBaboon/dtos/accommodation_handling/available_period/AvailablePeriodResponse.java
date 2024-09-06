package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

@Data
public class AvailablePeriodResponse {
    private Long id;
    private TimeSlot timeSlot;
    private Float PricePerNight;
}
