package rs.ac.uns.ftn.BookingBaboon.services.users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.tokens.EmailVerificationToken;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.PasswordChangeRequest;
import rs.ac.uns.ftn.BookingBaboon.repositories.users.IUserRepository;
import rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces.IReservationService;
import rs.ac.uns.ftn.BookingBaboon.services.tokens.ITokenService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IEmailService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IUserService;

import java.beans.Encoder;
import java.util.*;

@RequiredArgsConstructor
@Service
@Primary
public class UserService implements IUserService, UserDetailsService {

    private final IUserRepository repository;

    private final ITokenService tokenService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public Collection<User> getAll() {
        return new ArrayList<User>(repository.findAll());
    }

    @Override
    public User get(Long userId) throws ResponseStatusException {
        Optional<User> found = repository.findById(userId);
        if (found.isEmpty()) {
            String value = bundle.getString("user.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public User create(User user) throws ResponseStatusException {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
            repository.flush();
            return user;
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
    public User update(User user) throws ResponseStatusException {
        try {
            get(user.getId());
            repository.save(user);
            repository.flush();
            return user;
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
    public User remove(Long userId) {
        User found = get(userId);
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
    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User login(String email, String password) {
        User found = repository.findByEmail(email);
        if(found!=null && found.getPassword().equals(password)){
            return found;
        }
        return null;
    }

    @Override
    public User activate(String token) {

        EmailVerificationToken verificationToken = tokenService.getVerificationToken(token);
        if (verificationToken == null) {
            return null;
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return null;
        }

        user.activate();
        tokenService.delete(token);
        repository.save(user);
        return user;
    }


    @Override
    public User changePassword(Long userId, PasswordChangeRequest request) {
        User user = get(userId);

        request.setNewPassword(encoder.encode(request.getNewPassword()));

        if (encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            user.setPassword(request.getNewPassword());
            repository.save(user);
            repository.flush();
            return user;
        }
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);

        if (user != null) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .authorities(user.getRole().toString())
                    .build();
        }

        throw new UsernameNotFoundException("User not found with this username: " + username);
    }

    @Override
    public User toggleNotifications(Long userId, NotificationType notificationType) {
        User user = get(userId);
        Set<NotificationType> ignoredNotifications = user.getIgnoredNotifications();

        if (ignoredNotifications.contains(notificationType)) {
            ignoredNotifications.remove(notificationType);
        } else {
            ignoredNotifications.add(notificationType);
        }

        user.setIgnoredNotifications(ignoredNotifications);
        update(user);
        return user;
    }

    @Override
    public User blockUser(Long userId) {
        User user = get(userId);
        user.block();
        update(user);
        return user;
    }

    @Override
    public User unblockUser(Long userId) {
        User user = get(userId);
        user.unblock();
        update(user);
        return user;
    }

}
