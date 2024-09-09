package rs.ac.uns.ftn.BookingBaboon.pki.dtos;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class X500NameDto {
    private String email;
    private String commonName;
    private String organizationalUnit;
    private String organization;
    private String location;
    private String state;
    private String country;

    public X500NameDto(X500Name x500Name) {
        email = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.E)[0].getFirst().getValue());
        commonName = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.CN)[0].getFirst().getValue());
        organizationalUnit = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.OU)[0].getFirst().getValue());
        organization = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.O)[0].getFirst().getValue());
        location = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.L)[0].getFirst().getValue());
        state = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.ST)[0].getFirst().getValue());
        country = IETFUtils.valueToString(x500Name.getRDNs(BCStyle.C)[0].getFirst().getValue());
    }
}
