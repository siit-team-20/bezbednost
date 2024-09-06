package rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;

@Repository
public interface IAvailablePeriodRepository extends JpaRepository<AvailablePeriod, Long> {
}
