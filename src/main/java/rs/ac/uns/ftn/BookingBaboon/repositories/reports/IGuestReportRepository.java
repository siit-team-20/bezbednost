package rs.ac.uns.ftn.BookingBaboon.repositories.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.GuestReport;

@Repository
public interface IGuestReportRepository extends JpaRepository<GuestReport, Long> {
    Boolean existsByReporteeIdAndReportedGuestId(Long reporteeId, Long guestId);
}
