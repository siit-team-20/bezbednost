package rs.ac.uns.ftn.BookingBaboon.dtos.users.certadmins;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Role;

@Data
public class CertAdminCreateRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String password;
    private Role role;
    
}
