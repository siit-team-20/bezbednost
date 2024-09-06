package rs.ac.uns.ftn.BookingBaboon.dtos.reports;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReportStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserResponse;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestResponse;

import java.util.Date;
@Data
public class GuestReportResponse {
    private Long id;
    private UserReferenceRequest reportee;
    private Date createdOn;
    private ReportStatus status;
    private String message;
    private GuestReferenceRequest reportedGuest;
}
