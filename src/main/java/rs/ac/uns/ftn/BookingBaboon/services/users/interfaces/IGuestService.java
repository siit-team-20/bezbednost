package rs.ac.uns.ftn.BookingBaboon.services.users.interfaces;

import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Guest;

import java.util.Collection;

public interface IGuestService {
    Collection<Guest> getAll();

    Guest get(Long guestId) throws ResponseStatusException;

    Guest create(Guest guest) throws ResponseStatusException;

    Guest update(Guest guest) throws ResponseStatusException;

    Guest remove(Long guestId);

    Guest getProfile(String guestEmail);

    Collection<Accommodation> getFavorites(Long guestId);

    Guest addFavorite(Long guestId, Long accommodationId);
    Guest removeFavorite(Long guestId, Long accommodationId);

    Guest toggleNotifications(Long guestId, NotificationType notificationType);

    void removeAll();


}
