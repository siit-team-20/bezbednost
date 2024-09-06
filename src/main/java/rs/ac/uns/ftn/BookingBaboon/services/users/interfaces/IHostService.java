package rs.ac.uns.ftn.BookingBaboon.services.users.interfaces;

import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;

import java.util.Collection;

public interface IHostService {
    Collection<Host> getAll();

    Host get(Long hostId) throws ResponseStatusException;

    Host get(String hostEmail) throws ResponseStatusException;

    Host create(Host host) throws ResponseStatusException;

    Host update(Host host) throws ResponseStatusException;

    Host remove(Long hostId);

    Host getProfile(String hostEmail);

    Host toggleNotificaitons(Long hostId, NotificationType notificationType);

    Reservation handleReservation(Long reservationId, boolean isApproved);

    void removeAll();
}

