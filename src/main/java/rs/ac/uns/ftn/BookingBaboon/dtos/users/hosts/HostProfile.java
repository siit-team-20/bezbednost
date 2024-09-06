package rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.Set;

@Data
public class HostProfile {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;
}
