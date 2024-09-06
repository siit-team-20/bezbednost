package rs.ac.uns.ftn.BookingBaboon.domain.reviews;
import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;

@Entity
@Data
@Table(name = "host_reviews")
public class HostReview extends Review {

    @ManyToOne
    private Host reviewedHost;
}
