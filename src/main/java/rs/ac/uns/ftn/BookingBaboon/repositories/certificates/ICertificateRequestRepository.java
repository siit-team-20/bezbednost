package rs.ac.uns.ftn.BookingBaboon.repositories.certificates;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequest;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequestStatus;

@Repository
public interface ICertificateRequestRepository extends JpaRepository<CertificateRequest, Long> { 
    List<CertificateRequest> findAllByStatus(CertificateRequestStatus status);
 }
