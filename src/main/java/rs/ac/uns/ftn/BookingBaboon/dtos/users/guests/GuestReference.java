package rs.ac.uns.ftn.BookingBaboon.dtos.users.guests;

import lombok.Data;

@Data
public class GuestReference {
    private Long id;

    public GuestReference(){

    }
    public GuestReference(long id) {
        this.id = id;
    }
}
