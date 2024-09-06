package rs.ac.uns.ftn.BookingBaboon.dtos.users;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String currentPassword;
    private String newPassword;
}
