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
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class KeyStoreRepository {
    
    
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());
    private KeyStore keyStore;
    public static final String keyStoreDirectoryPath = "src/main/resources/keystore/";
    public static final String keyStoreFilePath = keyStoreDirectoryPath + "keystore.jks";

    public KeyStoreRepository() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            String value = bundle.getString("certificate.internalError");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, value);
        }
    }

    public void readKeyStore(String filename, char[] password) {
        try {
            if(filename != null) {
                keyStore.load(new FileInputStream(filename), password);
            } else {
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            String value = bundle.getString("certificate.internalError");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, value);
        }
    }

    public void writeKeyStore(String filename, char[] password) {
        try {
            keyStore.store(new FileOutputStream(filename), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            String value = bundle.getString("certificate.internalError");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, value);
        }
    }

    public Certificate readCertificate(String keyStorePassword, String alias) {
        readKeyStore(keyStoreFilePath, keyStorePassword.toCharArray());
        try {
            if (keyStore.isCertificateEntry(alias)) {
                return keyStore.getCertificate(alias);
            }
        } catch (KeyStoreException e) {
            String value = bundle.getString("certificate.internalError");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, value);
        }
        return null;
    }

    public void writeCertificate(String alias, X509Certificate certificate) {
        try {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException e) {
            String value = bundle.getString("certificate.internalError");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, value);
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
