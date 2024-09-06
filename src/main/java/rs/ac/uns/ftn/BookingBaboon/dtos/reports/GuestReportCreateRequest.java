package rs.ac.uns.ftn.BookingBaboon.dtos.reports;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReportStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestRequest;

import java.util.Date;
@Data

public class GuestReportCreateRequest {
    private UserReferenceRequest reportee;
    private Date createdOn;
    private ReportStatus status;
    private String message;
    private GuestReferenceRequest reportedGuest;
}
