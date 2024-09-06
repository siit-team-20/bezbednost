package rs.ac.uns.ftn.BookingBaboon.services.users.interfaces;

import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationModification;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.GuestReport;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Admin;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.util.Collection;

public interface IAdminService {
    Collection<Admin> getAll();

    Admin get(Long adminId) throws ResponseStatusException;

    Admin create(Admin admin) throws ResponseStatusException;

    Admin update(Admin admin) throws ResponseStatusException;

    Admin remove(Long adminId);

    User blockUser(Long userId);

    Collection<GuestReport> getAllReports();

    Collection<AccommodationModification> getAllAccommodationChanges();

    Accommodation approveAccommodationChange(Long accommodationId);

    Accommodation denyAccommodationChange(Long accommodationId);

    void removeAll();
}
