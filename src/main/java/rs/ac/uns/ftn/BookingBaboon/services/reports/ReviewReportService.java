package rs.ac.uns.ftn.BookingBaboon.services.reports;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReviewReport;
import rs.ac.uns.ftn.BookingBaboon.repositories.reports.IReviewReportRepository;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IReviewReportService;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ReviewReportService implements IReviewReportService {
    private final IReviewReportRepository repository;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    @Override
    public Collection<ReviewReport> getAll() {
        return repository.findAll();
    }

    @Override
    public ReviewReport get(Long reviewReportId) {
        Optional<ReviewReport> found = repository.findById(reviewReportId);
        if (found.isEmpty()) {
            String value = bundle.getString("reviewReport.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public ReviewReport create(ReviewReport reviewReport) {
        if(doesReportAlreadyExist(reviewReport.getReportedReview().getId(),reviewReport.getReportee().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report already exists!");
        }
        try {
            repository.save(reviewReport);
            repository.flush();
            return reviewReport;
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
    public ReviewReport update(ReviewReport reviewReport) {
        try {
            get(reviewReport.getId()); // this will throw GuestReportNotFoundException if reviewReport is not found
            repository.save(reviewReport);
            repository.flush();
            return reviewReport;
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
    public ReviewReport remove(Long reviewReportId) {
        ReviewReport found = get(reviewReportId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAllForGuest(Long guestid) {
        for(ReviewReport report : getAll()) {
            if (report.getReportedReview().getReviewer().getId().equals(guestid)) {
                remove(report.getId());
            }
        }
    }

    @Override
    public void removeAllByUser(Long userId) {
        for(ReviewReport report : getAll()) {
            if(report.getReportee().getId().equals(userId)) {
                remove(report.getId());
            }
        }
    }

    @Override
    public Boolean doesReportAlreadyExist(Long reviewId, Long reporteeId) {
        return repository.existsByReporteeIdAndReportedReviewId(reporteeId, reviewId);
    }
}
