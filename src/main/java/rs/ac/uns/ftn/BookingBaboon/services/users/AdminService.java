package rs.ac.uns.ftn.BookingBaboon.services.users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationModification;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.GuestReport;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Admin;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.repositories.users.IAdminRepository;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IAdminService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AdminService implements IAdminService {

    private final IAdminRepository repository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());


    @Override
    public Collection<Admin> getAll() {
        return new ArrayList<Admin>(repository.findAll());
    }

    @Override
    public Admin get(Long adminId) throws ResponseStatusException {
        Optional<Admin> found = repository.findById(adminId);
        if (found.isEmpty()) {
            String value = bundle.getString("admin.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Admin create(Admin admin) throws ResponseStatusException {
        try {
            admin.setPassword(encoder.encode(admin.getPassword()));
            repository.save(admin);
            repository.flush();
            return admin;
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
            StringBuilder sb = new StringBuilder(1000);
            for (ConstraintViolation<?> error : errors) {
                sb.append(error.getMessage()).append("\n");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
        }
    }

    @Override
    public Admin update(Admin admin) throws ResponseStatusException {
        try {
            Admin updatedAdmin = get(admin.getId());
            updatedAdmin.setFirstName(admin.getFirstName());
            updatedAdmin.setLastName(admin.getLastName());
            updatedAdmin.setEmail(admin.getEmail());
            updatedAdmin.setAddress(admin.getAddress());
            updatedAdmin.setPhoneNumber(admin.getPhoneNumber());
            repository.save(updatedAdmin);
            repository.flush();
            return admin;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = (RuntimeException) c;
            }
            if ((c != null) && (c instanceof ConstraintViolationException)) {
                ConstraintViolationException c2 = (ConstraintViolationException) c;
                Set<ConstraintViolation<?>> errors = c2.getConstraintViolations();
                StringBuilder sb = new StringBuilder(1000);
                for (ConstraintViolation<?> error : errors) {
                    sb.append(error.getMessage() + "\n");
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
            }
            throw ex;
        }
    }

    @Override
    public Admin remove(Long adminId) {
        Admin found = get(adminId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

    @Override
    public User blockUser(Long userId) {
        return new User();
    }

    @Override
    public Collection<GuestReport> getAllReports() {
        return new ArrayList<GuestReport>();
    }

    @Override
    public Collection<AccommodationModification> getAllAccommodationChanges() {
        return new ArrayList<AccommodationModification>();
    }



    @Override
    public Accommodation approveAccommodationChange(Long accommodationId) {
        return new Accommodation();
    }

    @Override
    public Accommodation denyAccommodationChange(Long accommodationId) {
        return new Accommodation();
    }


}
