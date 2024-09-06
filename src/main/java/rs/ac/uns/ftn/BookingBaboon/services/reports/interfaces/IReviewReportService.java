package rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.reports.HostReport;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReviewReport;

import java.util.Collection;

public interface IReviewReportService {
    Collection<ReviewReport> getAll();
    ReviewReport get(Long hostReportId);

    ReviewReport create(ReviewReport reviewReport);

    ReviewReport update(ReviewReport reviewReport);

    ReviewReport remove(Long reviewReportId);
    void removeAllForGuest(Long guestId);

    void removeAllByUser(Long userId);

    Boolean doesReportAlreadyExist(Long reviewId, Long reporteeId);
}
