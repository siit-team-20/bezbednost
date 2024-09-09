package rs.ac.uns.ftn.BookingBaboon.dtos.certificates;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequestStatus;

@Data
public class CertificateRequestCreateDTO {
    private Long id;
    private String subjectEmail; 
    private String subjectCommonName;
    private String subjectOrganization;
    private String subjectOrganizationUnit;
    private String subjectLocation;
    private String subjectState; 
    private String subjectCountry;
    private CertificateRequestStatus status;
}
