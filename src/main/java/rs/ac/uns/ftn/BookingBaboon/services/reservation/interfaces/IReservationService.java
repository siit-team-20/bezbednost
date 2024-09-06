package rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

import java.time.Month;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public interface IReservationService {
    public HashSet<Reservation> getAll();
    public Reservation get(Long reservationId);
    public Reservation create(Reservation reservation);
    public Reservation update(Reservation reservation);
    public Reservation remove(Long reservationId);
    public void removeAllForGuest(Long guestId);
    public void removeAll();
    public Reservation deny(Long id);
    public Reservation approveReservation(Long reservationId);
    public Reservation cancel(Long id);
    public void mergeAvailablePeriods(AvailablePeriod addedPeriod, List<AvailablePeriod> availablePeriods, Long accommodationId);
    public int getCancellationCountForUser(Long userId);
    public boolean isApproved(Long reservationId);
    void removeAllForAccommodation(Long accommodationId);
    Collection<Reservation> getAllByAccommodation(Long accommodationId);
    Collection<Reservation> getAllFinishedByAccommodationAndMonth(Long accommodationId, int year, int month);
    Collection<Reservation> getAllFinishedByAccommodationAndTimeSlot(Long accommodationId, TimeSlot timeSlot);
    public Reservation handleAutomaticAcceptance(Reservation reservation);
    public Collection<Reservation> getAllForGuest(Long id);
    public Collection<Reservation> getAllForHost(Long id);

    Collection<Reservation> cancelAllForGuest(Long guestId);
}
