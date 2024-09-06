package rs.ac.uns.ftn.BookingBaboon.dtos.notifications;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;

import java.util.Date;

@Data
public class NotificationCreateRequest {

    private String message;

    private NotificationType type;

    private Date timeCreated;

    private UserReferenceRequest user;
}
