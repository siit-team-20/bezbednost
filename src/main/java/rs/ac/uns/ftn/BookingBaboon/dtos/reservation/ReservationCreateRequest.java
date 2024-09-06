package rs.ac.uns.ftn.BookingBaboon.dtos.reservation;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestReference;

@Data
public class ReservationCreateRequest {
    private AccommodationReference accommodation;
    private TimeSlot timeSlot;
    private GuestReference guest;
    private Float price;

    public ReservationCreateRequest(AccommodationReference accommodationReference, TimeSlot timeSlot, GuestReference guestReference, Float price) {
        this.accommodation = accommodationReference;
        this.timeSlot = timeSlot;
        this.guest = guestReference;
        this.price = price;
    }
}
