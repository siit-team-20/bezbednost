package rs.ac.uns.ftn.BookingBaboon.pki.certificates;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Optional;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.BookingBaboon.domain.certificates.CertificateType;
import rs.ac.uns.ftn.BookingBaboon.dtos.certificates.CertificateCreateDTO;
import rs.ac.uns.ftn.BookingBaboon.pki.dtos.CertificateDTO;
import rs.ac.uns.ftn.BookingBaboon.pki.dtos.X500NameDto;
import rs.ac.uns.ftn.BookingBaboon.pki.repositories.KeyStoreRepository;
import rs.ac.uns.ftn.BookingBaboon.pki.repositories.PasswordRepository;
import rs.ac.uns.ftn.BookingBaboon.pki.repositories.PrivateKeyRepository;
import rs.ac.uns.ftn.BookingBaboon.pki.utils.DateConverter;
import rs.ac.uns.ftn.BookingBaboon.pki.utils.DateRange;

@Service
public class CertificateService {
    
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    private final String keyStoreName = "keystore";

    private final KeyStoreRepository keyStoreRepository;
    private final PasswordRepository passwordRepository;
    private final PrivateKeyRepository privateKeyRepository;
    
    public CertificateService(KeyStoreRepository keyStoreRepository, PasswordRepository passwordRepository, PrivateKeyRepository privateKeyRepository) {
        this.keyStoreRepository = keyStoreRepository;
        keyStoreRepository.readKeyStore(KeyStoreRepository.keyStoreFilePath, passwordRepository.getPassword(keyStoreName).toCharArray());
        this.passwordRepository = passwordRepository;
        this.privateKeyRepository = privateKeyRepository;
    }
    
    private X509Certificate getRoot(X509Certificate certificate) {
        X509Certificate issuer = getIssuer(certificate);
        if (issuer == null) {
            return null;
        }

        if (isRoot(issuer)) {
            return issuer;
        }
        else {
            return getRoot(issuer);
        }
    }

    private ArrayList<String> getCertificateExtensions(X509Certificate certificate) {
        ArrayList<String> extensions = new ArrayList<>();
        boolean[] keyUsages = certificate.getKeyUsage();
        if (keyUsages != null) {
            if (keyUsages[5])
                extensions.add("Certificate Signing (Path Length: -1)");
            if (keyUsages[0])
                extensions.add("Digital Signature");
            if (keyUsages[2])
                extensions.add("Key Encipherment");
        }

        try {
            List<String> extendedKeyUsages = certificate.getExtendedKeyUsage();
            if (extendedKeyUsages != null) {
                if (extendedKeyUsages.contains("1.3.6.1.5.5.7.3.1"))
                    extensions.add("Server Authentication");
            }
        }
        catch (CertificateParsingException e) {
            
        }

        try {
            Collection<List<?>> subjectAlternativeNames = certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames != null) {
                subjectAlternativeNames.forEach(subjectAlternativeName -> extensions.add("Domain = \"" + subjectAlternativeName.get(1).toString() + "\""));
            }
        }
        catch (CertificateParsingException e) {

        }
        
        return extensions;
    }

    private boolean isEndEntity(X509Certificate certificate) {
        boolean[] keyUsages = certificate.getKeyUsage();
        if (keyUsages == null) {
            return true;
        }

        return !certificate.getKeyUsage()[5];
    }

    private Set<X509Certificate> getCertificatesSignedBy(X509Certificate cert) {
        Set<X509Certificate> certs = new HashSet<>();
        for (Certificate c : keyStoreRepository.getAllCertificates()) {
            if (!(c instanceof X509Certificate x509Certificate))
                continue;

            if (!x509Certificate.equals(cert) && x509Certificate.getIssuerX500Principal().equals(cert.getSubjectX500Principal()))
                certs.add(x509Certificate);
        }

        return certs;
    }

    private CertificateDTO formCertificateTree(X509Certificate root) {
        JcaX509CertificateHolder jcaX509CertificateHolder = null;
        try {
            jcaX509CertificateHolder = new JcaX509CertificateHolder(root);
        }
        catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        
        CertificateDTO certificates = new CertificateDTO();
        certificates.setSerialNumber(root.getSerialNumber().toString(16));
        certificates.setSignatureAlgorithm(root.getSigAlgName());
        certificates.setIssuer(new X500NameDto(jcaX509CertificateHolder.getIssuer()));
        certificates.setStartDate(LocalDate.ofInstant(root.getNotBefore().toInstant(), ZoneId.systemDefault()));
        certificates.setEndDate(LocalDate.ofInstant(root.getNotAfter().toInstant(), ZoneId.systemDefault()));
        certificates.setSubject(new X500NameDto(jcaX509CertificateHolder.getSubject()));
        certificates.setExtensions(getCertificateExtensions(root));
        certificates.setEndEntity(isEndEntity(root));
        certificates.setRoot(isRoot(root));
        certificates.setChildren(new HashSet<>());

        Set<X509Certificate> certificatesSignedByRoot = getCertificatesSignedBy(root);

        if (certificatesSignedByRoot.isEmpty())
            return certificates;

        for (X509Certificate certificate: certificatesSignedByRoot) {
            certificates.getChildren().add(formCertificateTree(certificate));
        }

        return certificates;
    }

    public CertificateDTO getCertificates() {
        Optional<X509Certificate> certificate = keyStoreRepository.getAllCertificates().stream().filter(cert -> cert instanceof X509Certificate).map(cert -> (X509Certificate) cert).findAny();
        if (certificate.isEmpty())
            return null;

        X509Certificate root = getRoot(certificate.get());
        if (root == null) {
            return null;
        }

        return formCertificateTree(root);
    }

    public X509Certificate getIssuer(X509Certificate certificate) {
        for (Certificate keyStoreCertificate : keyStoreRepository.getAllCertificates()) {
            if (!(keyStoreCertificate instanceof X509Certificate x509Certificate))
                continue;
            if (x509Certificate.getSubjectX500Principal().equals(certificate.getIssuerX500Principal()))
                return x509Certificate;
        }
        return null;
    }

    public boolean isRoot(X509Certificate certificate) {
        try {
            certificate.verify(certificate.getPublicKey());
        } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                | SignatureException e) {
            e.printStackTrace();
        }
        return certificate.getSubjectX500Principal().equals(certificate.getIssuerX500Principal());
    }

    public boolean isCertificateValid(String alias) {
        X509Certificate cert = (X509Certificate) keyStoreRepository.readCertificate(passwordRepository.getPassword(keyStoreName), alias);
        if (cert == null) {
            String value = bundle.getString("certificate.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }

        X509Certificate parentCert = getIssuer(cert);

        while (!isRoot(cert)) {
            try {
                cert.checkValidity();
                cert.verify(parentCert.getPublicKey());
            } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                    | SignatureException e) {
                return false;
            }
            cert = parentCert;
            parentCert = getIssuer(cert);
        }

        try {
            cert.checkValidity();
            cert.verify(parentCert.getPublicKey());
        } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                | SignatureException e) {
            return false;
        }

        return true;
    }

    private X509Certificate getX509CertificateFromAlias(String alias) {
        Certificate certificate = keyStoreRepository.readCertificate(passwordRepository.getPassword(keyStoreName), alias);

        if (!(certificate instanceof X509Certificate x509Certificate)) {
            String value = bundle.getString("certificate.notFound");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, value);
        }
        
        return x509Certificate;
    }

    private DateRange getNewCertificateDateRange(X509Certificate certificateAuthority) {
        long start = DateConverter.convertToUnixTime(certificateAuthority.getNotBefore());
        long end = DateConverter.convertToUnixTime(certificateAuthority.getNotAfter());
        long mid = start + (end - start) / 2;

        long current = DateConverter.getCurrentUnixTime();

        if (current >= start && current <= mid)
            return new DateRange(new Date(start * 1000), new Date(mid * 1000));
        else
            return new DateRange(new Date((mid + 1) * 1000), new Date(end * 1000));
    }

    public static X500Name generateX500Name(X500NameDto x500NameDto) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.E, x500NameDto.getEmail());
        builder.addRDN(BCStyle.CN, x500NameDto.getCommonName());
        builder.addRDN(BCStyle.OU, x500NameDto.getOrganizationalUnit());
        builder.addRDN(BCStyle.O, x500NameDto.getOrganization());
        builder.addRDN(BCStyle.L, x500NameDto.getLocation());
        builder.addRDN(BCStyle.ST, x500NameDto.getState());
        builder.addRDN(BCStyle.C, x500NameDto.getCountry());
        return builder.build();
    }

    private KeyPair generateCertificateKeyPair(String certificateAlias, CertificateType certificateType) {
        KeyPairGenerator keyGenerator;
        SecureRandom random;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            keyGenerator.initialize(4096, random);

            KeyPair keyPair = keyGenerator.generateKeyPair();
            if (certificateType == CertificateType.Intermediate)
                this.privateKeyRepository.writePrivateKey(keyPair.getPrivate(), certificateAlias);

            return keyPair;
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X509v3CertificateBuilder getCertificateBuilder(
        CertificateType certificateType,
        String serialNumber,
        X509Certificate certificateAuthority,
        X500Name issuerName,
        X500NameDto subjectDto
    ) {
        X500Name subjectX500Name = generateX500Name(subjectDto);

        DateRange dateRange = getNewCertificateDateRange(certificateAuthority);

        KeyPair subjectKeys = generateCertificateKeyPair(
            issuerName
                .getRDNs(BCStyle.E)[0]
                .getFirst()
                .getValue()
                .toString() + "|" + serialNumber,
            certificateType
        );

        return new JcaX509v3CertificateBuilder(
            issuerName,
            new BigInteger(serialNumber, 16),
            dateRange.getStartDate(),
            dateRange.getEndDate(),
            subjectX500Name,
            subjectKeys.getPublic()
        );
    }

    private ContentSigner getContentSigner(String issuerPrivateKeyAlias) {
        PrivateKey issuerPrivateKey = this.privateKeyRepository.getPrivateKey(issuerPrivateKeyAlias);

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider("BC");
        try {
            return builder.build(issuerPrivateKey);
        }
        catch (OperatorCreationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private X509Certificate signAndBuildCertificate(X509v3CertificateBuilder certificateBuilder, ContentSigner contentSigner)  {
        X509CertificateHolder certHolder = certificateBuilder.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter().setProvider("BC");
        
        try {
            return certConverter.getCertificate(certHolder);
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CertificateCreateDTO createCertificate(CertificateCreateDTO certificateDTO) {
        if (!isCertificateValid(certificateDTO.getAlias())) {
            String value = bundle.getString("certificate.notValid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, value);
        }

        X509Certificate certificateAuthority = getX509CertificateFromAlias(certificateDTO.getAlias());

        JcaX509CertificateHolder certificateAuthorityHolder = null;
        try {
            certificateAuthorityHolder = new JcaX509CertificateHolder(certificateAuthority);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        X500Name issuerName = certificateAuthorityHolder.getSubject();

        String serialNumber = Long.toHexString(DateConverter.getCurrentUnixTimeMillis());

        X509v3CertificateBuilder certGenerator = getCertificateBuilder(
            certificateDTO.getCertificateType(),
            serialNumber,
            certificateAuthority,
            issuerName,
            certificateDTO.getSubject()
        );

        try {

            if (certificateDTO.getCertificateType().equals(CertificateType.Https)) {

                certGenerator.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage, true,
                    new org.bouncycastle.asn1.x509.KeyUsage(
                            org.bouncycastle.asn1.x509.KeyUsage.digitalSignature
                            | org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment
                    )
                );

                certGenerator.addExtension(org.bouncycastle.asn1.x509.Extension.extendedKeyUsage, true,
                    new org.bouncycastle.asn1.x509.ExtendedKeyUsage(
                            new org.bouncycastle.asn1.x509.KeyPurposeId[]{
                                    org.bouncycastle.asn1.x509.KeyPurposeId.id_kp_serverAuth
                            }
                    )
                );

                certGenerator.addExtension(org.bouncycastle.asn1.x509.Extension.subjectAlternativeName, false,
                    new org.bouncycastle.asn1.x509.GeneralNames(
                            new org.bouncycastle.asn1.x509.GeneralName(
                                    org.bouncycastle.asn1.x509.GeneralName.dNSName, certificateDTO.getDomain()
                            )
                    )
                );
            }
            else if (certificateDTO.getCertificateType().equals(CertificateType.DigitalSigning)) {
                certGenerator.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage, true,
                    new org.bouncycastle.asn1.x509.KeyUsage(
                            org.bouncycastle.asn1.x509.KeyUsage.digitalSignature)
                );
            }
            else if (certificateDTO.getCertificateType().equals(CertificateType.Intermediate)) {
                certGenerator.addExtension(org.bouncycastle.asn1.x509.Extension.basicConstraints, true, new org.bouncycastle.asn1.x509.BasicConstraints(-1));

                certGenerator.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage, true, new org.bouncycastle.asn1.x509.KeyUsage(org.bouncycastle.asn1.x509.KeyUsage.keyCertSign));
            }
        }
        catch (CertIOException e) {
            e.printStackTrace();
        }

        X509Certificate certificate = signAndBuildCertificate(certGenerator, getContentSigner(certificateDTO.getAlias()));

        keyStoreRepository.writeCertificate(issuerName.getRDNs(BCStyle.E)[0].getFirst().getValue().toString() + "|" + serialNumber, certificate);

        keyStoreRepository.writeKeyStore(KeyStoreRepository.keyStoreFilePath, passwordRepository.getPassword(keyStoreName).toCharArray());

        return new CertificateCreateDTO(
            certificateDTO.getCertificateType(),
            certificateAuthorityHolder
                .getIssuer()
                .getRDNs(BCStyle.E)[0]
                .getFirst()
                .getValue()
                .toString() + "|" + certificateAuthority.getSerialNumber().toString(16),
                certificateDTO.getSubject(),
                certificateDTO.getDomain()
        );
    }

}
