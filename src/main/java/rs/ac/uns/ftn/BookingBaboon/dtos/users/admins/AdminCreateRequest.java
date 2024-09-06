package rs.ac.uns.ftn.BookingBaboon.dtos.users.admins;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Role;

@Data
public class AdminCreateRequest {
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private Role role;
}
