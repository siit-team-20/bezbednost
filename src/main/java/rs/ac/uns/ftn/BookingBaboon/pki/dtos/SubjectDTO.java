package rs.ac.uns.ftn.BookingBaboon.pki.dtos;

import lombok.Data;

@Data
public class SubjectDTO {
    private String userId;
    private String email;
    private String commonName;
    private String surname;
    private String givenName;
    private String Organization;
    private String organizationalUnit;
    private String country;
   
}
