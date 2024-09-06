package rs.ac.uns.ftn.BookingBaboon.dtos.users.certadmins;

import lombok.Data;

@Data
public class CertAdminUpdateRequest {
    
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String jwt;

}
