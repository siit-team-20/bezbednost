package rs.ac.uns.ftn.BookingBaboon.controllers.users;

import java.util.Collection;
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

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.domain.users.CertAdmin;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.certadmins.CertAdminCreateRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.certadmins.CertAdminResponse;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.certadmins.CertAdminUpdateRequest;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.ICertAdminService;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/certadmin")
public class CertAdminController {
    
    private final ICertAdminService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<CertAdminResponse>> getCertAdmins() {
        Collection<CertAdmin> certAdmins = service.getAll();
        Collection<CertAdminResponse> certAdminResponses =  certAdmins.stream()
                .map(admin -> mapper.map(admin, CertAdminResponse.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(certAdminResponses, HttpStatus.OK);
    }

    @GetMapping({"/{adminId}"})
    public ResponseEntity<CertAdminResponse> get(@PathVariable Long adminId) {
        CertAdmin admin = service.get(adminId);
        if(admin==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>( mapper.map(admin, CertAdminResponse.class), HttpStatus.OK);
    }

    @PostMapping({"/"})
    public ResponseEntity<CertAdminResponse> create(@RequestBody CertAdminCreateRequest admin) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(admin, CertAdmin.class)),CertAdminResponse.class), HttpStatus.CREATED);
    }

    @PutMapping({"/"})
    public ResponseEntity<CertAdminResponse> update(@RequestBody CertAdminUpdateRequest admin) {
        return new ResponseEntity<>(mapper.map(service.update(mapper.map(admin, CertAdmin.class)),CertAdminResponse.class), HttpStatus.OK);
    }

    @DeleteMapping({"/{adminId}"})
    public ResponseEntity<CertAdminResponse> remove(@PathVariable Long adminId) {
        CertAdmin certAdmin = service.remove(adminId);
        if(certAdmin==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>( mapper.map(certAdmin,CertAdminResponse.class), HttpStatus.OK);
    }

}
