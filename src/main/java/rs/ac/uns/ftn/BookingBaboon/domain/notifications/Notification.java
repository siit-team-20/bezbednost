package rs.ac.uns.ftn.BookingBaboon.domain.notifications;

import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.util.Date;

@Entity
@Data
@Table(name = "notifications")
@TableGenerator(name="notifications_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="notification", initialValue = 5, valueColumnName="value_pk")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "notifications_id_generator")
    private Long id;

    private String message;

    @Enumerated
    private NotificationType type;

    private Date timeCreated;

    private Boolean isRead = false;

    public Notification(String message, NotificationType type, Date timeCreated, User user) {
        this.message = message;
        this.type = type;
        this.timeCreated = timeCreated;
        this.user = user;
    }

    public Notification() {}

    @ManyToOne
    private User user;
}
