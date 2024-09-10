package rs.ac.uns.ftn.BookingBaboon.services.certificates.interfaces;

import java.util.Collection;

import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.certificates.CertificateCreateDTO;

public interface ICertificateRequestService {
    
    Collection<CertificateRequest> getAll();

    CertificateRequest get(Long certificateRequestId) throws ResponseStatusException;

    CertificateRequest create(CertificateRequest certificateRequest) throws ResponseStatusException;

    CertificateRequest update(CertificateRequest certificateRequest) throws ResponseStatusException;

    CertificateRequest remove(Long certificateRequestId);

    CertificateRequest approve(Long certificateRequestId, CertificateCreateDTO certificateDTO) throws ResponseStatusException;

    CertificateRequest reject(Long certificateRequestId) throws ResponseStatusException;
    
}
