package rs.ac.uns.ftn.BookingBaboon.dtos.reviews;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.ReviewStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostRequest;

import java.util.Date;
@Data
public class HostReviewRequest {
    private UserReferenceRequest reviewer;
    private Date createdOn;
    private short rating;
    private String comment;
    private HostReferenceRequest reviewedHost;
    private ReviewStatus status;

}
