package rs.ac.uns.ftn.BookingBaboon.dtos.users.guests;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.Set;

@Data
public class GuestNotificationSettings {
    private Long id;
    private String email;

}
