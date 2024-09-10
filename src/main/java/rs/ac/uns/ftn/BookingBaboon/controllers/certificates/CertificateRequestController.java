package rs.ac.uns.ftn.BookingBaboon.controllers.certificates;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.certificates.CertificateCreateDTO;
import rs.ac.uns.ftn.BookingBaboon.dtos.certificates.CertificateRequestCreateDTO;
import rs.ac.uns.ftn.BookingBaboon.dtos.certificates.CertificateRequestResponseDTO;
import rs.ac.uns.ftn.BookingBaboon.services.certificates.interfaces.ICertificateRequestService;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/certificate-requests")
@SecurityRequirement(name = "Keycloak")
public class CertificateRequestController {
    private final ICertificateRequestService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<CertificateRequestResponseDTO>> getAll() {

        Collection<CertificateRequest> certificateRequests = service.getAll();

        return new ResponseEntity<>(certificateRequests.stream()
                .map(certificateRequest -> mapper.map(certificateRequest, CertificateRequestResponseDTO.class))
                .collect(Collectors.toList()), HttpStatus.OK);

    }

    @GetMapping({"/waiting"})
    public ResponseEntity<Collection<CertificateRequestResponseDTO>> getAllWaiting() {

        Collection<CertificateRequest> certificateRequests = service.getAllWaiting();

    
        return new ResponseEntity<>(certificateRequests.stream()
        .map(certificateRequest -> mapper.map(certificateRequest, CertificateRequestResponseDTO.class))
        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateRequestResponseDTO> get(@PathVariable Long id) {

        CertificateRequest certificateRequest = service.get(id);

        if (certificateRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mapper.map(certificateRequest, CertificateRequestResponseDTO.class), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<CertificateRequestResponseDTO> create(@RequestBody CertificateRequestCreateDTO certificateRequestDTO) {

        CertificateRequest result = service.create(mapper.map(certificateRequestDTO, CertificateRequest.class));
        return new ResponseEntity<>(mapper.map(result, CertificateRequestResponseDTO.class), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<CertificateRequestResponseDTO> update(@RequestBody CertificateRequest certificateRequest) {

        CertificateRequest result = service.update(mapper.map(certificateRequest, CertificateRequest.class));

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(mapper.map(result, CertificateRequestResponseDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {

        CertificateRequest certificateRequest = service.get(id);

        if (certificateRequest != null) {
            service.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } 
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CertificateRequestResponseDTO> approve(@PathVariable Long id, @RequestBody CertificateCreateDTO certificateDTO) {

        CertificateRequest certificateRequest = service.approve(id, certificateDTO);
        return new ResponseEntity<>(mapper.map(certificateRequest, CertificateRequestResponseDTO.class), HttpStatus.OK);

    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<CertificateRequestResponseDTO> reject(@PathVariable Long id) {

        CertificateRequest certificateRequest = service.reject(id);
        return new ResponseEntity<>(mapper.map(certificateRequest, CertificateRequestResponseDTO.class), HttpStatus.OK);

    }

}