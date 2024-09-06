package rs.ac.uns.ftn.BookingBaboon.services.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.domain.users.CertAdmin;
import rs.ac.uns.ftn.BookingBaboon.repositories.users.ICertAdminRepository;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.ICertAdminService;

@RequiredArgsConstructor
@Service
public class CertAdminService implements ICertAdminService {

    private final ICertAdminRepository repository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());


    @Override
    public Collection<CertAdmin> getAll() {
        return new ArrayList<CertAdmin>(repository.findAll());
    }

    @Override
    public CertAdmin get(Long certAdminId) throws ResponseStatusException {
        Optional<CertAdmin> found = repository.findById(certAdminId);
        if (found.isEmpty()) {
            String value = bundle.getString("admin.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public CertAdmin create(CertAdmin certAdmin) throws ResponseStatusException {
        try {
            certAdmin.setPassword(encoder.encode(certAdmin.getPassword()));
            repository.save(certAdmin);
            repository.flush();
            return certAdmin;
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
    public CertAdmin update(CertAdmin certAdmin) throws ResponseStatusException {
        try {
            CertAdmin updatedSysAdmin = get(certAdmin.getId());
            updatedSysAdmin.setFirstName(certAdmin.getFirstName());
            updatedSysAdmin.setLastName(certAdmin.getLastName());
            updatedSysAdmin.setEmail(certAdmin.getEmail());
            updatedSysAdmin.setAddress(certAdmin.getAddress());
            updatedSysAdmin.setPhoneNumber(certAdmin.getPhoneNumber());
            repository.save(updatedSysAdmin);
            repository.flush();
            return certAdmin;
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
    public CertAdmin remove(Long adminId) {
        CertAdmin found = get(adminId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

}
