package rs.ac.uns.ftn.BookingBaboon.dtos.reports;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reports.ReportStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.reviews.ReviewReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;

import java.util.Date;

@Data
public class ReviewReportUpdateRequest {
    private Long id;
    private UserReferenceRequest reportee;
    private Date createdOn;
    private ReportStatus status;
    private String message;
    private ReviewReferenceRequest reportedReview;
}
