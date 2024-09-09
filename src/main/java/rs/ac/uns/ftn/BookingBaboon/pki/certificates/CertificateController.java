package rs.ac.uns.ftn.BookingBaboon.pki.certificates;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.pki.dtos.CertificateDTO;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/certificates")
public class CertificateController {
    private final CertificateService service;

    @GetMapping
    public ResponseEntity<CertificateDTO> getAll() {

        CertificateDTO certificates = service.getCertificates();

        return new ResponseEntity<>(certificates, HttpStatus.OK);

    }

}
