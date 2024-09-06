package rs.ac.uns.ftn.BookingBaboon.repositories.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.Report;

@Repository
public interface IReportRepository extends JpaRepository<Report, Long> {
}
