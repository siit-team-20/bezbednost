package rs.ac.uns.ftn.BookingBaboon.repositories.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReviewReport;

public interface IReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    Boolean existsByReporteeIdAndReportedReviewId(Long reporteeId, Long reviewId);
}
