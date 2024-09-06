package rs.ac.uns.ftn.BookingBaboon.domain.reports;
import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;

@Entity
@Data
@Table(name = "host_reports")

public class HostReport extends Report {

    @ManyToOne
    private Host reportedHost;
}
