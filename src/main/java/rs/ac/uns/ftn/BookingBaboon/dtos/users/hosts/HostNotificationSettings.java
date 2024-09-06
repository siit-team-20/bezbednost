package rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.Set;

@Data
public class HostNotificationSettings {

    private Long id;

    private String email;


}
