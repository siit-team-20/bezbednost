package rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationModification;

import java.util.Collection;

@Repository
public interface IAccommodationModificationRepository extends JpaRepository<AccommodationModification, Long> {
    Collection<AccommodationModification> findAllByAccommodationId(Long accommodationId);
}
