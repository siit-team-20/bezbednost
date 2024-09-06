package rs.ac.uns.ftn.BookingBaboon.services.notifications;

import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.Notification;

import java.util.Collection;

public interface INotificationService {
    Collection<Notification> getAll();

    Notification get(Long notificationId) throws ResponseStatusException;

    Notification create(Notification notification) throws ResponseStatusException;

    Notification update(Notification notification) throws ResponseStatusException;

    Notification remove(Long notificationId);

    Collection<Notification> getByUserId(Long userId);

    void removeAll();

    void removeAllByUser(Long userId);
    Integer getUnreadCountByUserId(Long userId);
    Notification read(Long notificationId);
}
