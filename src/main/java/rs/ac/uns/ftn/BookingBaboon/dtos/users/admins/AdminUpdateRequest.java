package rs.ac.uns.ftn.BookingBaboon.dtos.users.admins;

import lombok.Data;

@Data
public class AdminUpdateRequest {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String jwt;
}
