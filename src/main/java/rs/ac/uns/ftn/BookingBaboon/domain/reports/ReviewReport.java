package rs.ac.uns.ftn.BookingBaboon.domain.reports;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.Review;

@Entity
@Data
@Table(name = "review_reports")
public class ReviewReport extends Report{
    @ManyToOne
    private Review reportedReview;
}
