package rs.ac.uns.ftn.BookingBaboon.services.certificates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequest;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequestStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.certificates.CertificateCreateDTO;
import rs.ac.uns.ftn.BookingBaboon.pki.certificates.CertificateService;
import rs.ac.uns.ftn.BookingBaboon.repositories.certificates.ICertificateRequestRepository;
import rs.ac.uns.ftn.BookingBaboon.services.certificates.interfaces.ICertificateRequestService;

@RequiredArgsConstructor
@Service
public class CertificateRequestService implements ICertificateRequestService {
    
    private final ICertificateRequestRepository repository;

    @Autowired
    private final CertificateService certificateService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public Collection<CertificateRequest> getAll() {
        return new ArrayList<CertificateRequest>(repository.findAll());
    }
    
    public Collection<CertificateRequest> getAllWaiting() {
        return new ArrayList<CertificateRequest>(repository.findAllByStatus(CertificateRequestStatus.WAITING));
    }

    @Override
    public CertificateRequest get(Long certificateRequestId) throws ResponseStatusException {
        Optional<CertificateRequest> found = repository.findById(certificateRequestId);
        if (found.isEmpty()) {
            String value = bundle.getString("certificateRequest.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public CertificateRequest create(CertificateRequest certificateRequest) throws ResponseStatusException{
        try {
            repository.save(certificateRequest);
            repository.flush();
            return certificateRequest;
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
    public CertificateRequest update(CertificateRequest certificateRequest) throws ResponseStatusException {
        try {
            get(certificateRequest.getId());
            repository.save(certificateRequest);
            repository.flush();
            return certificateRequest;
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
    public CertificateRequest remove(Long certificateRequestId) {
        CertificateRequest found = get(certificateRequestId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public CertificateRequest approve(Long certificateRequestId, CertificateCreateDTO certificateDTO) throws ResponseStatusException {
        Optional<CertificateRequest> found = repository.findById(certificateRequestId);
        if (found.isEmpty()) {
            String value = bundle.getString("certificateRequest.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        CertificateRequest certificateRequest = found.get();
        if (!certificateRequest.getStatus().equals(CertificateRequestStatus.WAITING)) {
            String value = bundle.getString("certificateRequest.notWaiting");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, value);
        }
        certificateRequest.setStatus(CertificateRequestStatus.APPROVED);
        certificateService.createCertificate(certificateDTO);
        repository.flush();
        return certificateRequest;
    }

    @Override
    public CertificateRequest reject(Long certificateRequestId) throws ResponseStatusException {
        Optional<CertificateRequest> found = repository.findById(certificateRequestId);
        if (found.isEmpty()) {
            String value = bundle.getString("certificateRequest.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        CertificateRequest certificateRequest = found.get();
        if (!certificateRequest.getStatus().equals(CertificateRequestStatus.WAITING)) {
            String value = bundle.getString("certificateRequest.notWaiting");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, value);
        }
        certificateRequest.setStatus(CertificateRequestStatus.REJECTED);
        repository.flush();
        return certificateRequest;
    }
    
}
