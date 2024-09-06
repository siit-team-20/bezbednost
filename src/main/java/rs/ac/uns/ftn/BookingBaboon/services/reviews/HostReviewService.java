package rs.ac.uns.ftn.BookingBaboon.services.reviews;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.Notification;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.HostReview;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.repositories.reviews.IHostReviewRepository;
import rs.ac.uns.ftn.BookingBaboon.services.notifications.INotificationService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces.IHostReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IUserService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class HostReviewService implements IHostReviewService {

    private final IHostReviewRepository repository;
    private final INotificationService notificationService;
    private final IUserService userService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    @Override
    public Collection<HostReview> getAll()  {
        List<HostReview> hostReviews = repository.findAll();
        return hostReviews;
    }

    @Override
    public HostReview get(Long hostReviewId) {
        Optional<HostReview> found = repository.findById(hostReviewId);
        if (found.isEmpty()) {
            String value = bundle.getString("hostReview.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public HostReview create(HostReview hostReview) {
        if(doesReviewAlreadyExist(hostReview.getReviewedHost().getId(),hostReview.getReviewer().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Host Review already exists!");
        }
        try {
            repository.save(hostReview);
            repository.flush();
            notificationService.create(new Notification("You have been review by "+ userService.get(hostReview.getReviewer().getId()).getEmail(), NotificationType.HostReview, new Date(), hostReview.getReviewedHost()));
            return hostReview;
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
    public HostReview update(HostReview hostReview) {
        try {
            get(hostReview.getId()); // this will throw HostReviewNotFoundException if hostReview is not found
            repository.save(hostReview);
            repository.flush();
            return hostReview;
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
    public HostReview remove(Long hostReviewId) {
        HostReview found = get(hostReviewId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeByHost(Long hostId) {
        for(HostReview review : getAll()) {
            if (review.getReviewedHost().getId().equals(hostId)) {
                remove(review.getId());
            }
        }
    }

    @Override
    public User getReviewer(Long hostReviewId) {
        Optional<HostReview> found = repository.findById(hostReviewId);
        if (found.isEmpty()) {
            String value = bundle.getString("hostReview.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        HostReview hostReview = found.get();
        return hostReview.getReviewer();
    }

    @Override
    public float getAverageRating(Long hostId) {

        Collection<HostReview> reviews = getAll();

        int reviewNumber = 0;
        int ratingSum = 0;
        for(HostReview review : reviews) {
            if (review.getReviewedHost().getId().equals(hostId)) {
                ratingSum += review.getRating();
                reviewNumber += 1;
            }
        }

        return (float) ratingSum / reviewNumber;
    }

    @Override
    public void removeAllByUser(Long userId) {
        Collection<HostReview> hostReviews = getAll();
        for(HostReview review : hostReviews) {
            if (review.getReviewer().getId().equals(userId)) {
                remove(review.getId());
            }
        }
    }

    @Override
    public Collection<HostReview> getReviewsByHost(Long hostId) {
        return repository.getHostReviewsByReviewedHostId(hostId);
    }

    @Override
    public Collection<HostReview> getReviewsByGuest(Long guestId) {
        return repository.getHostReviewsByReviewerId(guestId);
    }

    @Override
    public Boolean doesReviewAlreadyExist(Long hostId, Long reviewerId) {
        return repository.existsByReviewerIdAndReviewedHostId(reviewerId, hostId);
    }
}
