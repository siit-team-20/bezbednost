package rs.ac.uns.ftn.BookingBaboon.domain.reports;

import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Table(name = "reports")
@TableGenerator(name="report_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="report", initialValue = 12, valueColumnName="value_pk")

public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "report_id_generator")
    private Long id;

    @ManyToOne
    private User reportee;

    private Date createdOn;

    @Enumerated
    private ReportStatus status = ReportStatus.Pending;
    private String message;
}
