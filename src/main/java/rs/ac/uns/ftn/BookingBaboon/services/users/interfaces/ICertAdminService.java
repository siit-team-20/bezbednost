package rs.ac.uns.ftn.BookingBaboon.services.users.interfaces;

import java.util.Collection;

import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.BookingBaboon.domain.users.CertAdmin;

public interface ICertAdminService {
    Collection<CertAdmin> getAll();

    CertAdmin get(Long certAdminId) throws ResponseStatusException;
    
    CertAdmin create(CertAdmin certAdmin) throws ResponseStatusException;

    CertAdmin update(CertAdmin certAdmin) throws ResponseStatusException;

    CertAdmin remove(Long certAdminId);

    void removeAll();
}
