package rs.ac.uns.ftn.BookingBaboon.services.users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.ReservationStatus;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Guest;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestNotificationSettings;
import rs.ac.uns.ftn.BookingBaboon.repositories.users.IGuestRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.notifications.INotificationService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IGuestReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IHostReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IReviewReportService;
import rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces.IReservationService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.AccommodationReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces.IHostReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.tokens.ITokenService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IEmailService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IGuestService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GuestService implements IGuestService {

    private final IGuestRepository repository;

    private final IEmailService emailService;

    private final ITokenService tokenService;

    private final IReservationService reservationService;
    private final IGuestReportService guestReportService;
    private final IHostReportService hostReportService;
    private final INotificationService notificationService;
    private final AccommodationReviewService accommodationReviewService;
    private final IHostReviewService hostReviewService;
    private final IReviewReportService reviewReportService;
    private final IAccommodationService accommodationService;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());


    @Override
    public Collection<Guest> getAll() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public Guest get(Long guestId) throws ResponseStatusException{
        Optional<Guest> found = repository.findById(guestId);
        if (found.isEmpty()) {
            String value = bundle.getString("guest.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Guest create(Guest guest) throws ResponseStatusException{
        try {
            guest.setPassword(encoder.encode(guest.getPassword()));
            repository.save(guest);
            repository.flush();
            emailService.sendActivationEmail(guest);
            return guest;
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
    public Guest update(Guest guest) throws ResponseStatusException{
        try {
            Guest updatedGuest = get(guest.getId());
            updatedGuest.setFirstName(guest.getFirstName());
            updatedGuest.setLastName(guest.getLastName());
            updatedGuest.setEmail(guest.getEmail());
            updatedGuest.setAddress(guest.getAddress());
            updatedGuest.setPhoneNumber(guest.getPhoneNumber());
            repository.save(updatedGuest);
            repository.flush();
            return guest;
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
    public Guest remove(Long guestId) {
        Guest found = get(guestId);
        for(Reservation reservation : reservationService.getAll()) {
            if (reservation.getGuest().getId().equals(guestId) && reservationService.isApproved(reservation.getId())) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Guest has active reservations");
            }
        }
        reviewReportService.removeAllForGuest(guestId);
        reservationService.removeAllForGuest(guestId);
        guestReportService.removeAllForGuest(guestId);
        hostReportService.removeAllByUser(guestId);
        notificationService.removeAllByUser(guestId);
        accommodationReviewService.removeAllByUser(guestId);
        hostReviewService.removeAllByUser(guestId);
        tokenService.delete(found);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

    @Override
    public Guest getProfile(String guestEmail) {
        Guest found = repository.findByEmail(guestEmail);
        if (found == null) {
            String value = bundle.getString("host.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found;
    }

    @Override
    public Collection<Accommodation> getFavorites(Long guestId) {
        Guest guest = get(guestId);
        return guest.getFavorites();
    }

    @Override
    public Guest addFavorite(Long guestId, Long accommodationId) {
        Guest guest = get(guestId);
        Accommodation accommodation = accommodationService.get(accommodationId);
        guest.addFavorite(accommodation);
        update(guest);
        return guest;
    }

    @Override
    public Guest removeFavorite(Long guestId, Long accommodationId) {
        Guest guest = get(guestId);
        Accommodation accommodation = accommodationService.get(accommodationId);
        guest.removeFavorite(accommodation);
        update(guest);
        return guest;
    }

    @Override
    public Guest toggleNotifications(Long guestId, NotificationType notificationType) {
        Guest guest = get(guestId);
        Set<NotificationType> ignoredNotifications = guest.getIgnoredNotifications();

        if (ignoredNotifications.contains(notificationType)) {
            ignoredNotifications.remove(notificationType);
        } else {
            ignoredNotifications.add(notificationType);
        }

        guest.setIgnoredNotifications(ignoredNotifications);
        update(guest);
        return guest;
    }


}
