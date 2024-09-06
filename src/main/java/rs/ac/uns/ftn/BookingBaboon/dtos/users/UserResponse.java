package rs.ac.uns.ftn.BookingBaboon.dtos.users;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String email;

    private String jwt;

}
