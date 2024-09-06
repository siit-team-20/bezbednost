package rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.*;

import java.util.*;


@Repository
public interface IAccommodationRepository extends JpaRepository<Accommodation, Long> {


    @Query("SELECT DISTINCT a FROM Accommodation a " +
            "WHERE (:#{#filter.city} IS NULL OR a.location.city = :#{#filter.city}) AND" +
            "(:#{#filter.guestNum} IS NULL OR a.minGuests <= :#{#filter.guestNum}) AND" +
            "(:#{#filter.guestNum} IS NULL OR a.maxGuests >= :#{#filter.guestNum}) AND" +
            "(:#{#filter.types} IS NULL OR a.type IN :#{#filter.types}) AND" +
            "(:#{#filter.minRating} IS NULL OR (SELECT AVG(r.rating) FROM AccommodationReview r WHERE r.reviewedAccommodation.id = a.id) >= :#{#filter.minRating}) AND" +
            "(:#{#filter.amenities} IS NULL OR (SELECT COUNT(DISTINCT amenity.name) FROM a.amenities amenity WHERE amenity.name IN :#{#filter.amenities}) = :#{#filter.amenities?.size()})"
    )
    List<Accommodation> findAccommodationsByFilter(
            @Param("filter") AccommodationFilter filter
    );

    @Query("SELECT ap FROM Accommodation a JOIN a.availablePeriods ap WHERE a.id = :accommodationId ORDER BY ap.timeSlot.startDate")
    List<AvailablePeriod> findAvailablePeriodsSortedByStartDate(@Param("accommodationId") Long accommodationId);

    Set<Accommodation> findAllByHostId(Long hostId);
}