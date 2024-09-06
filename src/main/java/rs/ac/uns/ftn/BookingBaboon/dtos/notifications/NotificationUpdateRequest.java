package rs.ac.uns.ftn.BookingBaboon.dtos.notifications;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

@Data
public class NotificationUpdateRequest {
    private Long id;

    private String message;

    private NotificationType type;
}
