package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

@Data
public class AvailablePeriodCreateRequest{
        private TimeSlot timeSlot;
        private Float PricePerNight;

        public AvailablePeriodCreateRequest(TimeSlot timeSlot, float pricePerNight) {
                this.timeSlot = timeSlot;
                this.PricePerNight = pricePerNight;
        }
}
