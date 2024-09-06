package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation;

import lombok.Data;

@Data
public class AccommodationReference {
    private Long id;

    public AccommodationReference(){}
    public AccommodationReference(long id) {
        this.id = id;
    }
}
