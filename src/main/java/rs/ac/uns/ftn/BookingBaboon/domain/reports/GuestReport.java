package rs.ac.uns.ftn.BookingBaboon.domain.reports;
import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Guest;

@Entity
@Data
@Table(name = "guest_reports")
public class GuestReport extends Report {

    @ManyToOne
    private Guest reportedGuest;
}
