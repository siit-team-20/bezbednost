package rs.ac.uns.ftn.BookingBaboon.dtos.users.certadmins;

import lombok.Data;

@Data
public class CertAdminResponse {

    private Long id;

    private String email;

    private String jwt;
}
