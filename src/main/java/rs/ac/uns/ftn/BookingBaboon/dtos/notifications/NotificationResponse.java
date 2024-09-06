package rs.ac.uns.ftn.BookingBaboon.dtos.notifications;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserResponse;

import java.util.Date;

@Data
public class NotificationResponse {

    private Long id;

    private String message;

    private NotificationType type;

    private Boolean isRead;

    private Date timeCreated;

    private UserReferenceRequest user;

}
