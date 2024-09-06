package rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.reviews.AccommodationReview;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.util.Collection;
import java.util.List;

public interface IAccommodationReviewService {
    Collection<AccommodationReview> getAll();
    AccommodationReview get(Long hostReviewId);

    AccommodationReview create(AccommodationReview accommodationReview);

    AccommodationReview update(AccommodationReview accommodationReview);

    AccommodationReview remove(Long accommodationReviewId);
    void removeFromAccommodation(Long accommodationId);

    User getReviewer(Long id);

    float getAverageRating(Long accommodationId);

    void removeAllByUser(Long userId);

    List<AccommodationReview> getAccommodationReviews(Long accommodationId);

    List<AccommodationReview> getAccommodationReviewsByGuest(Long guestId);

    Boolean doesReviewAlreadyExist(Long accommodationId, Long reviewerId);
}
