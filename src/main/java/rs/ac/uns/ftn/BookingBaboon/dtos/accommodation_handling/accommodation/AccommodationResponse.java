package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationType;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Amenity;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Location;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period.AvailablePeriodResponse;
import rs.ac.uns.ftn.BookingBaboon.dtos.shared.ImageReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.shared.ImageResponse;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostResponse;

import java.util.Set;

@Data
public class AccommodationResponse {
    private Long id;
    private String name;
    private String description;
    private HostResponse host;
    private Location location;
    private Set<Amenity> amenities;
    private Set<AvailablePeriodResponse> availablePeriods;
    private Integer minGuests;
    private Integer maxGuests;
    private Boolean isPricingPerPerson;
    private AccommodationType type;
    private Boolean isAutomaticallyAccepted;
    private Set<ImageReference> images;
    private Boolean isBeingEdited;
    private int cancellationDeadline;
}
