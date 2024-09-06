package rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts;

import lombok.Data;

@Data
public class HostResponse {

    private Long id;

    private String email;

    private String jwt;
}
