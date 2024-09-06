package rs.ac.uns.ftn.BookingBaboon.dtos.users.guests;

import lombok.Data;

@Data
public class GuestResponse {

    private Long id;

    private String email;

    private String jwt;

}
