package rs.ac.uns.ftn.BookingBaboon.services.reports;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.GuestReport;
import rs.ac.uns.ftn.BookingBaboon.repositories.reports.IGuestReportRepository;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IGuestReportService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GuestReportService implements IGuestReportService {

    private final IGuestReportRepository repository;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    @Override
    public Collection<GuestReport> getAll() {
        return repository.findAll();
    }

    @Override
    public GuestReport get(Long guestReportId) {
        Optional<GuestReport> found = repository.findById(guestReportId);
        if (found.isEmpty()) {
            String value = bundle.getString("guestReport.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public GuestReport create(GuestReport guestReport) {
        if(doesReportAlreadyExist(guestReport.getReportedGuest().getId(),guestReport.getReportee().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report already exists!");
        }
        try {
            repository.save(guestReport);
            repository.flush();
            return guestReport;
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
    public GuestReport update(GuestReport guestReport) {
        try {
            get(guestReport.getId()); // this will throw GuestReportNotFoundException if guestReport is not found
            repository.save(guestReport);
            repository.flush();
            return guestReport;
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
    public GuestReport remove(Long guestReportId) {
        GuestReport found = get(guestReportId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAllForGuest(Long guestid) {
        for(GuestReport report : getAll()) {
            if (report.getReportedGuest().getId().equals(guestid)) {
                remove(report.getId());
            }
        }
    }

    @Override
    public void removeAllByUser(Long userId) {
        for(GuestReport report : getAll()) {
            if(report.getReportee().getId().equals(userId)) {
                remove(report.getId());
            }
        }
    }

    @Override
    public Boolean doesReportAlreadyExist(Long guestId, Long reporteeId) {
        return repository.existsByReporteeIdAndReportedGuestId(reporteeId, guestId);
    }
}
