package rs.ac.uns.pki.certificates;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IUserService;

@RequiredArgsConstructor
@Service
public class CertificateService {

    private final IUserService userService;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

}
