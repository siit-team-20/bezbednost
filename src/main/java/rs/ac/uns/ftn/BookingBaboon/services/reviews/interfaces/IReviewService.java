package rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces;


import rs.ac.uns.ftn.BookingBaboon.domain.reviews.Review;

import java.util.Collection;
import java.util.Set;

public interface IReviewService {

    Set<Review> getAll();

    Review get(Long reviewId);

    Review create(Review review);

    Review update(Review review);

    Review remove(Long reviewId);
}
