package rs.ac.uns.ftn.BookingBaboon.dtos.users;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String jwt;
}
