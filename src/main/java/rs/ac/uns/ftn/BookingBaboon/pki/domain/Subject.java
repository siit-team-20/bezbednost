package rs.ac.uns.ftn.BookingBaboon.pki.domain;

import java.security.PublicKey;

import org.bouncycastle.asn1.x500.X500Name;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    private PublicKey publicKey;
    private X500Name x500Name;
}
