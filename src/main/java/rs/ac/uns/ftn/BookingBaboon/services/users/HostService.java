package rs.ac.uns.ftn.BookingBaboon.services.users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.repositories.users.IHostRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.notifications.INotificationService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IGuestReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IHostReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IReviewReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces.IReservationService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces.IAccommodationReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces.IHostReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.tokens.ITokenService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IEmailService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IHostService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class HostService implements IHostService {

    private final IHostRepository repository;

    private final IEmailService emailService;

    private final ITokenService tokenService;
    private final IAccommodationService accommodationService;
    private final IReservationService reservationService;
    private final IHostReviewService hostReviewService;
    private final IHostReportService hostReportService;
    private final IGuestReportService guestReportService;
    private final IAccommodationReviewService accommodationReviewService;
    private final IReviewReportService reviewReportService;
    private final INotificationService notificationService;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public Collection<Host> getAll() {
        return new ArrayList<Host>(repository.findAll());
    }

    @Override
    public Host get(Long hostId) throws ResponseStatusException {
        Optional<Host> found = repository.findById(hostId);
        if (found.isEmpty()) {
            String value = bundle.getString("host.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();    }

    @Override
    public Host get(String hostEmail) throws ResponseStatusException {
        return repository.findByEmail(hostEmail);
    }

    @Override
    public Host create(Host host) throws ResponseStatusException {
        try {
            host.setPassword(encoder.encode(host.getPassword()));
            repository.save(host);
            repository.flush();
            emailService.sendActivationEmail(host);
            return host;
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
    public Host update(Host host) throws ResponseStatusException {
        try {
            Host updatedHost = get(host.getId());
            updatedHost.setFirstName(host.getFirstName());
            updatedHost.setLastName(host.getLastName());
            updatedHost.setEmail(host.getEmail());
            updatedHost.setAddress(host.getAddress());
            updatedHost.setPhoneNumber(host.getPhoneNumber());
            repository.save(updatedHost);
            repository.flush();
            return host;
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
    public Host remove(Long hostId) {
        Host found = get(hostId);
        //for every reservation of every host's accommodation check if any of them is active
        for(Reservation reservation : reservationService.getAll()) {
            if (accommodationService.getAllByHost(hostId).contains(reservation.getAccommodation()) &&
                    reservationService.isApproved(reservation.getId())) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Host has accommodations with active reservations");
            }
        }
        //if no active reservations in any of the host's accommodations
        reviewReportService.removeAllByUser(hostId);
        hostReviewService.removeByHost(hostId);
        removeReservationsForAccommodations(hostId);
        accommodationService.removeAllByHost(hostId);
        hostReportService.removeAllForHost(hostId);
        notificationService.removeAllByUser(hostId);
        accommodationReviewService.removeAllByUser(hostId);

        for(Accommodation accommodation: accommodationService.getAllByHost(hostId)){
            reservationService.removeAllForAccommodation(accommodation.getId());
        }

        guestReportService.removeAllByUser(hostId);
        tokenService.delete(found);
        repository.delete(found);
        repository.flush();
        return found;
    }

    private void removeReservationsForAccommodations(Long hostId) {
        for(Accommodation accommodation : accommodationService.getAllByHost(hostId)) {
            reservationService.removeAllForAccommodation(accommodation.getId());
        }
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

    @Override
    public Host getProfile(String hostEmail) {
        Host found = repository.findByEmail(hostEmail);
        if (found == null) {
            String value = bundle.getString("host.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found;
    }

    @Override
    public Host toggleNotificaitons(Long hostId, NotificationType notificationType) {
        Host host = get(hostId);
        Set<NotificationType> ignoredNotifications = host.getIgnoredNotifications();

        if (ignoredNotifications.contains(notificationType)) {
            ignoredNotifications.remove(notificationType);
        } else {
            ignoredNotifications.add(notificationType);
        }

        host.setIgnoredNotifications(ignoredNotifications);
        update(host);
        return host;
    }

    @Override
    public Reservation handleReservation(Long reservationId, boolean isApproved) {
        return new Reservation();
    }


}
