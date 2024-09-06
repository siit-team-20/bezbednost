package rs.ac.uns.ftn.BookingBaboon.dtos.users;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
