package rs.ac.uns.ftn.BookingBaboon.dtos.reviews;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.ReviewStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostResponse;

import java.util.Date;
@Data
public class HostReviewResponse {
    private Long id;
    private UserReferenceRequest reviewer;
    private Date createdOn;
    private short rating;
    private String comment;
    private HostReference reviewedHost;
    private ReviewStatus status;
}
