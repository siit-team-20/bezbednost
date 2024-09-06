package rs.ac.uns.ftn.BookingBaboon.dtos.users.admins;

import lombok.Data;

@Data
public class AdminResponse {

    private Long id;

    private String email;

    private String jwt;
}
