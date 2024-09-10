package rs.ac.uns.ftn.BookingBaboon.dtos.certificates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateType;
import rs.ac.uns.ftn.BookingBaboon.pki.dtos.X500NameDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificateCreateDTO {
    private CertificateType certificateType;
    private String alias;
    private X500NameDto subject;
    private String domain;
}
