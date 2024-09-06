package rs.ac.uns.ftn.BookingBaboon.dtos.users;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserProfile {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;
    private Set<NotificationType> ignoredNotifications = new HashSet<NotificationType>();
}
