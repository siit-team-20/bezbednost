package rs.ac.uns.ftn.BookingBaboon.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "hosts")
public class Host extends User {

}
