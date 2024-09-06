package rs.ac.uns.ftn.BookingBaboon.services.reviews;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.Notification;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.AccommodationReview;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAccommodationRepository;
import rs.ac.uns.ftn.BookingBaboon.repositories.reviews.IAccommodationReviewRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.notifications.INotificationService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces.IAccommodationReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IUserService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AccommodationReviewService implements IAccommodationReviewService {

    private final IAccommodationReviewRepository repository;
    private final INotificationService notificationService;
    private final IUserService userService;
    private final IAccommodationRepository accommodationRepository;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    @Override
    public Collection<AccommodationReview> getAll() {
        return repository.findAll();
    }

    @Override
    public AccommodationReview get(Long accommodationReviewId) {
        Optional<AccommodationReview> found = repository.findById(accommodationReviewId);
        if (found.isEmpty()) {
            String value = bundle.getString("accommodationReview.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public AccommodationReview create(AccommodationReview accommodationReview) {
        if(doesReviewAlreadyExist(accommodationReview.getReviewedAccommodation().getId(),accommodationReview.getReviewer().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Host Review already exists!");
        }
        try {
            repository.save(accommodationReview);
            repository.flush();
            Accommodation accommodation = accommodationRepository.findById(accommodationReview.getReviewedAccommodation().getId()).get();
            notificationService.create(new Notification("Your accommodation " + accommodation.getName() + " has been reviewed by "+ userService.get(accommodationReview.getReviewer().getId()).getEmail(), NotificationType.AccommodationReview, new Date(), accommodation.getHost()));
            return accommodationReview;
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
            StringBuilder sb = new StringBuilder(1000);
            for (ConstraintViolation<?> error : errors) {
                sb.append(error.getMessage()).append("\n");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
        }
    }

    @Override
    public AccommodationReview update(AccommodationReview accommodationReview) {
        try {
            get(accommodationReview.getId()); // this will throw AccommodationReviewNotFoundException if accommodationReview is not found
            repository.save(accommodationReview);
            repository.flush();
            return accommodationReview;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = (RuntimeException) c;
            }
            if ((c != null) && (c instanceof ConstraintViolationException)) {
                ConstraintViolationException c2 = (ConstraintViolationException) c;
                Set<ConstraintViolation<?>> errors = c2.getConstraintViolations();
                StringBuilder sb = new StringBuilder(1000);
                for (ConstraintViolation<?> error : errors) {
                    sb.append(error.getMessage() + "\n");
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
            }
            throw ex;
        }
    }

    @Override
    public AccommodationReview remove(Long accommodationReviewId) {
        AccommodationReview found = get(accommodationReviewId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeFromAccommodation(Long accommodationId) {

        for(AccommodationReview review : getAll()) {
            if(review.getReviewedAccommodation().getId().equals(accommodationId)) {
                remove(review.getId());
            }
        }
    }

    @Override
    public User getReviewer(Long accommodationReviewId) {
        Optional<AccommodationReview> found = repository.findById(accommodationReviewId);
        if (found.isEmpty()) {
            String value = bundle.getString("accommodationReview.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        AccommodationReview accommodationReview = found.get();
        return accommodationReview.getReviewer();
    }

    @Override
    public float getAverageRating(Long accommodationId) {

    Collection<AccommodationReview> reviews = getAll();

    int reviewNumber = 0;
    int ratingSum = 0;
    for(AccommodationReview review : reviews) {
        if (review.getReviewedAccommodation().getId().equals(accommodationId)) {
            ratingSum += review.getRating();
            reviewNumber += 1;
        }
    }
    if(reviewNumber == 0) return -1;
    return (float) ratingSum / reviewNumber;
    }

    @Override
    public void removeAllByUser(Long userId) {
        for(AccommodationReview accommodationReview : getAll()) {
            if (accommodationReview.getReviewer().getId().equals(userId)) {
                remove(accommodationReview.getId());
            }
        }
    }

    @Override
    public List<AccommodationReview> getAccommodationReviews(Long accommodationId) {
        return repository.findAccommodationReviewsByReviewedAccommodation_Id(accommodationId);
    }

    @Override
    public List<AccommodationReview> getAccommodationReviewsByGuest(Long guestId) {
        return repository.findAccommodationReviewsByReviewerId(guestId);
    }

    @Override
    public Boolean doesReviewAlreadyExist(Long accommodationId, Long reviewerId) {
        return repository.existsByReviewerIdAndReviewedAccommodationId(reviewerId, accommodationId);
    }
}
