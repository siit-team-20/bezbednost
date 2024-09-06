package rs.ac.uns.ftn.BookingBaboon.dtos.reservation;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestResponse;

@Data
public class ReservationRequest {
    private Long id;
    private AccommodationRequest accommodation;
    private TimeSlot timeSlot;
    private GuestResponse guest;
    private Float price;
}
