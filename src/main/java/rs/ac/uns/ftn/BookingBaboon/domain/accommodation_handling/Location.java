package rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class Location {

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String address;
}
