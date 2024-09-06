package rs.ac.uns.ftn.BookingBaboon.services.reservation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.Notification;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.ReservationStatus;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.repositories.reservation_handling.IReservationRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAvailablePeriodService;
import rs.ac.uns.ftn.BookingBaboon.services.notifications.INotificationService;
import rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces.IReservationService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IUserService;

import java.time.LocalDate;
import java.util.*;
@RequiredArgsConstructor
@Service
public class ReservationService implements IReservationService {
    private final IReservationRepository repository;
    private final IAccommodationService accommodationService;
    private final IAvailablePeriodService availablePeriodService;
    private final INotificationService notificationService;
    private final IUserService userService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public HashSet<Reservation> getAll() {
        return new HashSet<Reservation>(repository.findAll());
    }

    @Override
    public Reservation get(Long reservationId) {
        Optional<Reservation> found = repository.findById(reservationId);
        if (found.isEmpty()) {
            String value = bundle.getString("reservation.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Reservation create(Reservation reservation) {
        try {
            repository.save(reservation);
            repository.flush();
            Accommodation accommodation = accommodationService.get(reservation.getAccommodation().getId());
            Host host = accommodation.getHost();
            notificationService.create(new Notification("A reservation request for " + accommodation.getName() + " from " + reservation.getTimeSlot().getStartDate() + " to " + reservation.getTimeSlot().getEndDate() + " has been created", NotificationType.ReservationCreated, new Date(), userService.get(host.getId())));
            return reservation;
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
            StringBuilder sb = new StringBuilder(1000);
            for (ConstraintViolation<?> error : errors) {
                sb.append(error.getMessage()).append("\n");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
        }
    }

    @Override
    public Reservation update(Reservation reservation) {
        try {
            get(reservation.getId()); // this will throw ReservationNotFoundException if reservation is not found
            repository.save(reservation);
            repository.flush();
            return reservation;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = (RuntimeException) c;
            }
            if ((c != null) && (c instanceof ConstraintViolationException)) {
                ConstraintViolationException c2 = (ConstraintViolationException) c;
                Set<ConstraintViolation<?>> errors = c2.getConstraintViolations();
                StringBuilder sb = new StringBuilder(1000);
                for (ConstraintViolation<?> error : errors) {
                    sb.append(error.getMessage() + "\n");
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
            }
            throw ex;
        }
    }

    @Override
    public Reservation remove(Long reservationId) {
        Reservation found = get(reservationId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAllForGuest(Long guestId) {
        for(Reservation reservation : getAll()) {
            if (reservation.getGuest().getId().equals(guestId)) {
                remove(reservation.getId());
            }
        }
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }
    @Override
    public Reservation deny(Long reservationId) {
        Reservation found = get(reservationId);
        found.Deny();
        update(found);
        notificationService.create(new Notification("Your reservation for " + accommodationService.get(found.getAccommodation().getId()).getName() + " from " + found.getTimeSlot().getStartDate() + " to " + found.getTimeSlot().getEndDate() + " has been denied", NotificationType.ReservationRequestResponse, new Date(), userService.get(found.getGuest().getId())));
        return found;
    }

    private void denyOverlappingReservations(TimeSlot timeSlot, Long accommodationId, Long skipId) {
        Collection<Reservation> reservations = repository.findAllByAccommodationId(accommodationId);
        for(Reservation reservation : reservations) {
            if (reservation.getTimeSlot().overlaps(timeSlot) && reservation.getId() != skipId) {
                deny(reservation.getId());
            }
        }
    }

    @Override
    public int getCancellationCountForUser(Long userId) {
        return repository.findAllByGuest_IdAndStatus(userId, ReservationStatus.Canceled).size();
    }

    @Override
    public boolean isApproved(Long reservationId) {
        return get(reservationId).getStatus().equals(ReservationStatus.Approved);
    }

    @Override
    public void removeAllForAccommodation(Long accommodationId) {
        for(Reservation reservation : getAll()) {
            if (reservation.getAccommodation().getId().equals(accommodationId)) {
                remove(reservation.getId());
            }
        }
    }

    @Override
    public Collection<Reservation> getAllByAccommodation(Long accommodationId) {
        return repository.findAllByAccommodationId(accommodationId);
    }

    @Override
    public Collection<Reservation> getAllFinishedByAccommodationAndMonth(Long accommodationId, int year, int month) {
        return repository.findAllByAccommodationIdAndMonth(accommodationId, year, month, ReservationStatus.Finished);
    }

    @Override
    public Collection<Reservation> getAllFinishedByAccommodationAndTimeSlot(Long accommodationId, TimeSlot timeSlot) {
        return repository.findAllByAccommodationIdAndDates(accommodationId, timeSlot.getStartDate(), timeSlot.getEndDate(), ReservationStatus.Finished);
    }

    public Reservation approveReservation(Long reservationId){
        Reservation reservation = get(reservationId);
        reservation.Approve();
        Accommodation accommodation = accommodationService.get(reservation.getAccommodation().getId());

        List<AvailablePeriod> overlappingPeriods = availablePeriodService.getOverlappingPeriods(reservation.getTimeSlot(), accommodation.getAvailablePeriods());
        List<AvailablePeriod> newAvailablePeriods = availablePeriodService.splitPeriods(reservation.getTimeSlot(), overlappingPeriods);

        //Delete the old ones
        for(AvailablePeriod oldPeriod: overlappingPeriods){
            accommodationService.removePeriod(oldPeriod.getId(), accommodation.getId());
/*
            availablePeriodService.remove(oldPeriod.getId());
*/
        }

        //Add new ones
        for(AvailablePeriod newAvailablePeriod: newAvailablePeriods){
            AvailablePeriod result = availablePeriodService.create(newAvailablePeriod);
            accommodationService.addPeriod(result.getId(), accommodation.getId());
        }

        denyOverlappingReservations(reservation.getTimeSlot(), accommodation.getId(), reservation.getId());
        notificationService.create(new Notification("Your reservation for " + accommodationService.get(reservation.getAccommodation().getId()).getName() + " from " + reservation.getTimeSlot().getStartDate() + " to " + reservation.getTimeSlot().getEndDate() + " has been approved", NotificationType.ReservationRequestResponse, new Date(), userService.get(reservation.getGuest().getId())));

        update(reservation);
        return reservation;
    }

    @Override
    public Reservation cancel(Long id) {
        Reservation reservation = get(id);

        if(reservation.getStatus().equals(ReservationStatus.Canceled) ||
                reservation.getStatus().equals(ReservationStatus.Finished) ||
                reservation.getStatus().equals(ReservationStatus.Denied)) {
            return null;
        }

        int deadlineDays = reservation.getAccommodation().getCancellationDeadline();
        if (reservation.getTimeSlot().getStartDate().toEpochDay() - LocalDate.now().toEpochDay() <= deadlineDays && reservation.getStatus() == ReservationStatus.Approved) {
            return null;
        }

        if(reservation.getStatus().equals(ReservationStatus.Approved)) {
            /*AvailablePeriod addedPeriod = availablePeriodService.create(new AvailablePeriod(reservation.getTimeSlot(), getPricePerNight(reservation.getId())));
            accommodationService.addPeriod(addedPeriod.getId(), reservation.getAccommodation().getId());*/
            AvailablePeriod addedPeriod = new AvailablePeriod(reservation.getTimeSlot(), getPricePerNight(reservation.getId()));
            mergeAvailablePeriods(addedPeriod, reservation.getAccommodation().getAvailablePeriods(), reservation.getAccommodation().getId());
        }
        reservation.Cancel();
        notificationService.create(new Notification("Reservation for " + accommodationService.get(reservation.getAccommodation().getId()).getName() + " from " + reservation.getTimeSlot().getStartDate() + " to " + reservation.getTimeSlot().getEndDate() + " has been cancelled", NotificationType.ReservationCancelled, new Date(), userService.get(reservation.getAccommodation().getHost().getId())));
        repository.save(reservation);
        repository.flush();
        return reservation;
    }

    private Float getPricePerNight(Long id) {
        Reservation reservation = get(id);
        return reservation.getPrice() / reservation.getTimeSlot().getNumberOfDays();
    }

    @Override
    public void mergeAvailablePeriods(AvailablePeriod addedPeriod, List<AvailablePeriod> availablePeriods, Long accommodationId) {
        AvailablePeriod mergedPeriod = new AvailablePeriod();
        mergedPeriod.setTimeSlot(addedPeriod.getTimeSlot());
        mergedPeriod.setPricePerNight(addedPeriod.getPricePerNight());
        List<AvailablePeriod> deletedPeriods = new ArrayList<>();

        for (AvailablePeriod period : availablePeriods) {
            //if start = end and prices are the same -> then merge
            if (addedPeriod.getTimeSlot().getStartDate().equals(period.getTimeSlot().getEndDate()) && addedPeriod.getPricePerNight().equals(period.getPricePerNight())) {
                TimeSlot timeSlot = new TimeSlot(period.getTimeSlot().getStartDate(), mergedPeriod.getTimeSlot().getEndDate());
                mergedPeriod.setTimeSlot(timeSlot);
                deletedPeriods.add(period);
            }

            if (addedPeriod.getTimeSlot().getEndDate().equals(period.getTimeSlot().getStartDate()) && addedPeriod.getPricePerNight().equals(period.getPricePerNight())) {
                TimeSlot timeSlot = new TimeSlot(mergedPeriod.getTimeSlot().getStartDate(), period.getTimeSlot().getEndDate());
                mergedPeriod.setTimeSlot(timeSlot);
                deletedPeriods.add(period);
            }
        }

        for(AvailablePeriod period : deletedPeriods) {
            accommodationService.removePeriod(period.getId(), accommodationId);
        }

        AvailablePeriod savedPeriod = availablePeriodService.create(mergedPeriod);
        accommodationService.addPeriod(savedPeriod.getId(), accommodationId);
    }

    @Override
    public Reservation handleAutomaticAcceptance(Reservation reservation){
        Accommodation accommodation = accommodationService.get(reservation.getAccommodation().getId());
        if(accommodation.getIsAutomaticallyAccepted()){
            Reservation result = approveReservation(reservation.getId());
            return result;
        }

        return reservation;

    }

    @Override
    public Collection<Reservation> getAllForGuest(Long id) {
        return repository.findAllByGuest_Id(id);
    }

    @Override
    public Collection<Reservation> getAllForHost(Long id) {
        return repository.findAllByAccommodation_Host_Id(id);
    }

    @Override
    public Collection<Reservation> cancelAllForGuest(Long guestId) {
        Collection<Reservation> reservations = repository.findAllByGuest_Id(guestId);
        for(Reservation reservation : reservations) {
            cancel(reservation.getId());
        }
        return reservations;
    }

}