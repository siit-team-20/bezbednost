package rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.reviews.HostReview;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.util.Collection;

public interface IHostReviewService {

    Collection<HostReview> getAll();

    HostReview get(Long hostReviewId);

    HostReview create(HostReview hostReview);

    HostReview update(HostReview hostReview);

    HostReview remove(Long hostReviewid);
    void removeByHost(Long hostId);

    User getReviewer(Long id);

    float getAverageRating(Long id);

    void removeAllByUser(Long userId);
    Collection<HostReview> getReviewsByHost(Long hostId);

    Collection<HostReview> getReviewsByGuest(Long guestId);

    Boolean doesReviewAlreadyExist(Long hostId, Long reviewerId);
}
