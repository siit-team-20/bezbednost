package rs.ac.uns.ftn.BookingBaboon.pki.dtos;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CertificateDTO {
    private String serialNumber;
    private String signatureAlgorithm;
    private X500NameDto issuer;
    private LocalDate startDate;
    private LocalDate endDate;
    private X500NameDto subject;
    private ArrayList<String> extensions;
    private boolean isEndEntity;
    private boolean isRoot;
    private Set<CertificateDTO> children = new HashSet<>();
}
