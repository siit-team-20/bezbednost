package rs.ac.uns.ftn.BookingBaboon.dtos.certificates;

import java.util.Date;
import java.util.List;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateExtension;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequest;

@Data
public class CertificateCreateDTO {
    private String alias;
    private String subjectCommonName;
    private String subjectSurname;
    private String subjectGivenName;
    private String subjectOrganization;
    private String subjectOrganizationUnit;
    private String subjectCountry;
    private String subjectEmail; 
    private String subjectUserId; 
    private String issuerAlias;
    private Date startDate;
    private Date endDate;
    private List<CertificateExtension> extensions;

    public CertificateCreateDTO(CertificateRequest request){
        alias = request.getAlias();
        subjectCommonName = request.getSubjectCommonName();
        subjectSurname = request.getSubjectSurname();
        subjectGivenName = request.getSubjectGivenName();
        subjectOrganization = request.getSubjectOrganization();
        subjectOrganizationUnit = request.getSubjectOrganizationUnit();
        subjectCountry = request.getSubjectCountry();
        subjectEmail = request.getSubjectEmail();
        subjectUserId = request.getSubjectUserId();
        issuerAlias = request.getIssuerAlias();
        startDate = request.getStartDate();
        endDate = request.getEndDate();
        extensions = request.getExtensions();
    }
}
