package rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationFilter;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface IAccommodationService {
    public HashSet<Accommodation> getAll();

    public Set<Accommodation> getAllByHost(Long hostId);

    public Accommodation get(Long accommodationId);

    public Accommodation create(Accommodation accommodation);

    public Accommodation update(Accommodation accommodation);

    public Accommodation remove(Long accommodationId);

    public void removeAll();

    public AccommodationFilter parseFilter(String city, String checkin, String checkout, Integer guestNum, Double minPrice, Double maxPrice, String propertyType, String amenities, Double minRating);

    public Collection<Accommodation> search(AccommodationFilter filter);

    public void removeAllByHost(Long hostId);

    public float getTotalPrice(Accommodation accommodation, TimeSlot desiredPeriod);
    public LocalDate parseDate(String date);

    Accommodation addImage(Long imageId, Long accommodationId);

    Accommodation addPeriod(Long periodId, Long accommodationId);

    Accommodation removePeriod(Long periodId, Long accommodationId);

    Accommodation updateEditingStatus(Long accommodationId, boolean isBeingEdited);

    Accommodation updateAutoAccept(Long accommodationId, Boolean isAutomaticallyAccepted);

    Accommodation updateCancellationDeadline(Long accommodationId, int value);
}
