package rs.ac.uns.ftn.BookingBaboon.repositories.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.HostReport;

@Repository
public interface IHostReportRepository extends JpaRepository<HostReport, Long> {
    Boolean existsByReporteeIdAndReportedHostId(Long reporteeId, Long hostId);
}