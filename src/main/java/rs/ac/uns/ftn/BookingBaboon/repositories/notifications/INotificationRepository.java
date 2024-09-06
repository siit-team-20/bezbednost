package rs.ac.uns.ftn.BookingBaboon.repositories.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.Notification;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    Integer countByUserIdAndIsReadFalse(@Param("userId") Long userId);

/*    @Query("SELECT COUNT(n) FROM Notification n " +
            "WHERE n.id = :userId " +
            "AND n.isRead = :false " +
            "AND n.type NOT IN :ignoredTypes")
    Integer countUnreadNotifications(
            @Param("userId") Long userId,
            @Param("ignoredTypes") Set<NotificationType> ignoredTypes
    );*/

    Integer countByUserIdAndIsReadFalseAndTypeNotIn(
            @Param("userId") Long userId,
            @Param("ignoredTypes") Collection<NotificationType> ignoredTypes
    );
    Collection<Notification> findAllByUserIdAndTypeNotIn(Long userId, Collection<NotificationType> ignoredTypes);

    Collection<Notification> findAllByUserId(Long userId);
}
