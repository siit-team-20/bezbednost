package rs.ac.uns.ftn.BookingBaboon.services.notifications;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.Notification;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Guest;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Role;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.repositories.notifications.INotificationRepository;
import rs.ac.uns.ftn.BookingBaboon.services.users.UserService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IGuestService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IHostService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IUserService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class NotificationService implements INotificationService {

    private final INotificationRepository repository;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public Collection<Notification> getAll() {
        return new ArrayList<Notification>(repository.findAll());
    }

    @Override
    public Notification get(Long notificationId) throws ResponseStatusException{
        Optional<Notification> found = repository.findById(notificationId);
        if (found.isEmpty()) {
            String value = bundle.getString("notification.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Notification create(Notification notification) throws ResponseStatusException{
        try {
            repository.save(notification);
            repository.flush();
            sendNotification(notification);
            return notification;
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
    public Notification update(Notification notification) throws ResponseStatusException {
        try {
            get(notification.getId());
            repository.save(notification);
            repository.flush();
            return notification;
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
    public Notification remove(Long notificationId) {
        Notification found = get(notificationId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public Collection<Notification> getByUserId(Long userId) {
        User user = userService.get(userId);
        if (user.getIgnoredNotifications().size() == 0) {
            return repository.findAllByUserId(userId);
        }
        return repository.findAllByUserIdAndTypeNotIn(userId, new ArrayList<>(user.getIgnoredNotifications()));
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

    @Override
    public void removeAllByUser(Long userId) {
        for(Notification notification : getAll()) {
            if (notification.getUser().getId().equals(userId)) {
                remove(notification.getId());
            }
        }
    }

    @Override
    public Integer getUnreadCountByUserId(Long userId) {
        User user = userService.get(userId);
        if (user.getIgnoredNotifications().size() == 0) {
            return repository.countByUserIdAndIsReadFalse(userId);
        }
        return repository.countByUserIdAndIsReadFalseAndTypeNotIn(userId, new ArrayList<>(user.getIgnoredNotifications()));
    }

    @Override
    public Notification read(Long notificationId) {
        Notification notification = get(notificationId);
        notification.setIsRead(true);
        Notification result = update(notification);
        return result;
    }

    private void sendNotification(Notification notification) {
        simpMessagingTemplate.convertAndSend("/notification-publisher/" + notification.getUser().getId(), notification);
    }

}
