package rs.ac.uns.ftn.BookingBaboon.dtos.reports;

import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReportStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostReferenceRequest;

import java.util.Date;

public class UserReportResponse {
    private Long id;
    private UserReferenceRequest reportee;
    private Date createdOn;
    private ReportStatus status;
    private String message;
    private HostReferenceRequest reportedUser;
}
