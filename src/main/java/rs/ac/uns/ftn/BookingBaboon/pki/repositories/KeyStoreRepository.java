package rs.ac.uns.ftn.BookingBaboon.pki.repositories;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

@Repository
public class KeyStoreRepository {
    private KeyStore keyStore;
    public static final String keyStoreDirectoryPath = "src/main/resources/keystore/";
    public static final String keyStoreFilePath = keyStoreDirectoryPath + "keystore.jks";

    public KeyStoreRepository() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void readKeyStore(String filename, char[] password) {
        if (filename != null) {
            try {
                keyStore.load(new FileInputStream(filename), password);
            } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                keyStore.load(null, password);
            } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeKeyStore(String filename, char[] password) {
        try {
            keyStore.store(new FileOutputStream(filename), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public Certificate readCertificate(String keyStorePassword, String alias) {
        readKeyStore(keyStoreFilePath, keyStorePassword.toCharArray());
        try {
            if (keyStore.isCertificateEntry(alias)) {
                return keyStore.getCertificate(alias);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeCertificate(String alias, X509Certificate certificate) {
        try {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public Set<Certificate>  getAllCertificates() {
        Set<Certificate> certificates = new HashSet<>();
        try {
        Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate certificate = keyStore.getCertificate(alias);
                certificates.add(certificate);
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return certificates;
    }
}
