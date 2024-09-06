package rs.ac.uns.ftn.BookingBaboon.dtos.users.admins;

import lombok.Data;

@Data
public class UserBlockResponse {

    private Long id;

    private String email;

    private boolean isBlocked;

}
