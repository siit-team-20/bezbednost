package rs.ac.uns.ftn.BookingBaboon.domain.reviews;
import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;

@Entity
@Data
@Table(name = "accommodation_reviews")
public class AccommodationReview extends Review {

    @ManyToOne
    private Accommodation reviewedAccommodation;
}
