package rs.ac.uns.ftn.BookingBaboon.repositories.reservation_handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.ReservationStatus;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    Collection<Reservation> findAllByAccommodationId(Long accommodationId);

    Collection<Reservation> findAllByGuest_Id(Long guest_id);

    Collection<Reservation> findAllByAccommodation_Host_Id(Long accommodation_host_id);
  
    @Query("SELECT r FROM Reservation r WHERE r.guest.id = :guest_id AND r.status = :status ")
    Collection<Reservation> findAllByGuest_IdAndStatus( @Param("guest_id") Long guest_id,
                                                        @Param("status") ReservationStatus status);



    @Query("SELECT r FROM Reservation r " +
            "WHERE r.accommodation.id = :accommodationId " +
            "AND YEAR(r.timeSlot.startDate) = :year " +
            "AND MONTH(r.timeSlot.startDate) = :month " +
            "AND r.status = :status")
    Collection<Reservation> findAllByAccommodationIdAndMonth(
            @Param("accommodationId") Long accommodationId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("status") ReservationStatus status
    );

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.accommodation.id = :accommodationId " +
            "AND r.timeSlot.startDate BETWEEN :startDate AND :endDate " +
            "AND r.status = :status")
    Collection<Reservation> findAllByAccommodationIdAndDates(
            @Param("accommodationId") Long accommodationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ReservationStatus status
    );

}
