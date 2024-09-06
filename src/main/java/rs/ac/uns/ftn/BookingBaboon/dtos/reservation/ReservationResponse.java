package rs.ac.uns.ftn.BookingBaboon.dtos.reservation;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.ReservationStatus;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Guest;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationResponse;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestResponse;

@Data
public class ReservationResponse {
    private Long id;
    private Accommodation accommodation;
    private TimeSlot timeSlot;
    private Guest guest;
    private Float price;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String  status;
}
