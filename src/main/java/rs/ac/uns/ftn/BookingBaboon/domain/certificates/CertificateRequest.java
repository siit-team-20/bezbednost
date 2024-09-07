package rs.ac.uns.ftn.BookingBaboon.domain.certificates;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.Data;

@Entity
@Data
@Table(name = "certificate_requests")
@TableGenerator(name="certificate_request_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="certificate_request", initialValue = 0, valueColumnName="value_pk")
public class CertificateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "certificate_request_id_generator")
    private Long id;
    private String subjectCommonName;
    private String alias;
    private String issuerAlias;
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
    private CertificateRequestStatus status = CertificateRequestStatus.WAITING;
}
