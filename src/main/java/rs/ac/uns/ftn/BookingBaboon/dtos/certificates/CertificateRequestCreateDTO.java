package rs.ac.uns.ftn.BookingBaboon.dtos.certificates;

import java.util.Date;
import java.util.List;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateExtension;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequestStatus;

@Data
public class CertificateRequestCreateDTO {
    private String alias;
    private String subjectCommonName;
    private String subjectSurname;
    private String subjectGivenName;
    private String subjectOrganization;
    private String subjectOrganizationUnit;
    private String subjectCountry;
    private String subjectEmail; 
    private String subjectUserId; 
    private Date startDate;
    private Date endDate;
    private List<CertificateExtension> extensions;
    private CertificateRequestStatus status;
}
