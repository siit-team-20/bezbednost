package rs.ac.uns.ftn.BookingBaboon.pki.repositories;


import java.security.PrivateKey;
import java.security.cert.Certificate;

import rs.ac.uns.ftn.BookingBaboon.pki.domain.Issuer;

public interface ICertificateRepository {
    public Certificate readCertificate(String keyStorePass, String alias);

    public Issuer readIssuerFromStore(String alias, char[] keyStorePassword, char[] privateKeyPassword);

    public PrivateKey readPrivateKey(String keyStorePass, String alias, String pass);

    public void write(String alias, PrivateKey issuerPrivateKey, char[] privateKeyPassword, Certificate certificate, char[] keyStorePassword);
}
