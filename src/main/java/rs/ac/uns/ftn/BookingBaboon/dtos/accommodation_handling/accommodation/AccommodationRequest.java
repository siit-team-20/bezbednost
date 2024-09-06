package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationType;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Location;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.amenity.AmenityRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period.AvailablePeriodRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostReference;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostRequest;

import java.util.Set;

@Data
public class AccommodationRequest {
    private Long id;
    private String name;
    private String description;
    private HostReference host;
    private Location location;
    private Set<AmenityRequest> amenities;
    private Set<AvailablePeriodRequest> availablePeriods;
    private Integer minGuests;
    private Integer maxGuests;
    private Boolean isPricingPerPerson;
    private AccommodationType type;
    private boolean isAutomaticallyAccepted;
    private Boolean isBeingEdited;
    private int cancellationDeadline;
}
