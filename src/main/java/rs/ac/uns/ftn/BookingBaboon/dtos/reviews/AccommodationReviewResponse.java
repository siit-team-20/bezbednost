package rs.ac.uns.ftn.BookingBaboon.dtos.reviews;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.reviews.ReviewStatus;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationResponse;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserReferenceRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserResponse;

import java.util.Date;

@Data
public class AccommodationReviewResponse {
    private Long id;
    private UserReferenceRequest reviewer;
    private Date createdOn;
    private short rating;
    private String comment;
    private AccommodationReference reviewedAccommodation;
    private ReviewStatus status;
}
