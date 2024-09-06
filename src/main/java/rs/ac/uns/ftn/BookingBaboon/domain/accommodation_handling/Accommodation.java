package rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;

import java.util.List;

@Entity
@Data
@Table(name = "accommodations")
@TableGenerator(name="accommodation_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="accommodation", initialValue = 20, valueColumnName="value_pk")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "accommodation_id_generator")
    private Long id;

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToOne
    private Host host;

    @Embedded
    private Location location;

    @ManyToMany
    private List<Amenity> amenities;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AvailablePeriod> availablePeriods;

    @Min(1)
    private Integer minGuests;

    @Min(1)
    private Integer maxGuests;

    private Boolean isPricingPerPerson;

    @Enumerated
    private AccommodationType type;
    private Boolean isAutomaticallyAccepted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    private Boolean isBeingEdited = true;
    private int cancellationDeadline;

    public void AddAmenity(Amenity amenity){
        amenities.add(amenity);
    }
    public void RemoveAmenity(Amenity amenity){
        amenities.remove(amenity);
    }
}
