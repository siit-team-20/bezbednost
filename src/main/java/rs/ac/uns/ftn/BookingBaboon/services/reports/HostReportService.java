package rs.ac.uns.ftn.BookingBaboon.services.reports;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.HostReport;
import rs.ac.uns.ftn.BookingBaboon.repositories.reports.IHostReportRepository;
import rs.ac.uns.ftn.BookingBaboon.services.reports.interfaces.IHostReportService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class HostReportService implements IHostReportService {

    private final IHostReportRepository repository;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    @Override
    public Collection<HostReport> getAll() {
        return repository.findAll();
    }

    @Override
    public HostReport get(Long hostReportId) {
        Optional<HostReport> found = repository.findById(hostReportId);
        if (found.isEmpty()) {
            String value = bundle.getString("notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public HostReport create(HostReport hostReport) {
        if(doesReportAlreadyExist(hostReport.getReportedHost().getId(),hostReport.getReportee().getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report already exists!");
        }
        try {
            repository.save(hostReport);
            repository.flush();
            return hostReport;
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
    public HostReport update(HostReport hostReport) {
        try {
            get(hostReport.getId()); // this will throw HostReportNotFoundException if hostReport is not found
            repository.save(hostReport);
            repository.flush();
            return hostReport;
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
    public HostReport remove(Long hostReportId) {
        HostReport found = get(hostReportId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAllForHost(Long hostId) {
        for(HostReport hostReport : getAll()) {
            if (hostReport.getReportedHost().getId().equals(hostId)) {
                remove(hostReport.getId());
            }
        }
    }

    @Override
    public void removeAllByUser(Long userId) {
        Collection<HostReport> reports = getAll();
        for(HostReport report : reports) {
            if (report.getReportee().getId().equals(userId)) {
                remove(report.getId());
            }
        }
    }

    @Override
    public Boolean doesReportAlreadyExist(Long hostId, Long reporteeId) {
        return repository.existsByReporteeIdAndReportedHostId(reporteeId, hostId);
    }
}
