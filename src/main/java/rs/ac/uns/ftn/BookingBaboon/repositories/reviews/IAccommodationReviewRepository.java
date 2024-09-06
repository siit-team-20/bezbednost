package rs.ac.uns.ftn.BookingBaboon.repositories.reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.AccommodationReview;

import java.util.List;

@Repository
public interface IAccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
    public List<AccommodationReview> findAccommodationReviewsByReviewedAccommodation_Id(Long reviewedAccommodation_id);

    List<AccommodationReview> findAccommodationReviewsByReviewerId(Long guestId);

    Boolean existsByReviewerIdAndReviewedAccommodationId(Long reviewerId, Long accommodationId);
}
