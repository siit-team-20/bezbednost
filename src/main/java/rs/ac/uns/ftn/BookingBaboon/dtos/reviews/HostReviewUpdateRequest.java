package rs.ac.uns.ftn.BookingBaboon.dtos.reviews;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.ReviewStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostReferenceRequest;

import java.util.Date;
@Data
public class HostReviewUpdateRequest {
    private Long id;
    private UserReferenceRequest reviewer;
    private Date createdOn;
    private short rating;
    private String comment;
    private HostReferenceRequest reviewedHost;
    private ReviewStatus status;

}
