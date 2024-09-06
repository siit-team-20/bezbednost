package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation_modification;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.*;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.shared.ImageReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostReference;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AccommodationModificationRequest {
    private Long id;
    private AccommodationReference accommodation;
    private String name;
    private String description;
    private HostReference host;
    private Location location;
    private Set<Amenity> amenities;
    private Set<AvailablePeriod> availablePeriods;
    private Integer minGuests;
    private Integer maxGuests;
    private Boolean isPricingPerPerson;
    private AccommodationType type;
    private boolean isAutomaticallyAccepted;
    private Set<ImageReference> images;
    private Date requestDate;
    private AccommodationModificationStatus status;
    private AccommodationModificationType requestType;
    private Boolean isBeingEdited;
    private int cancellationDeadline;
}
