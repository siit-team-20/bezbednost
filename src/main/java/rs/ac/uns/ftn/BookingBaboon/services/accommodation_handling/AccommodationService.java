package rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.ReservationStatus;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.*;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAccommodationRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationModificationService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAmenityService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAvailablePeriodService;
import rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces.IReservationService;
import rs.ac.uns.ftn.BookingBaboon.services.reviews.interfaces.IAccommodationReviewService;
import rs.ac.uns.ftn.BookingBaboon.services.shared.IImageService;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AccommodationService implements IAccommodationService {
    private final IAccommodationRepository repository;
    private final IAmenityService amenityService;
    private final IAccommodationReviewService accommodationReviewService;
    private final IImageService imageService;
    private final IAvailablePeriodService periodService;
    private final IAccommodationModificationService modificationService;


    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public HashSet<Accommodation> getAll() {
        return new HashSet<Accommodation>(repository.findAll());
    }

    @Override
    public Set<Accommodation> getAllByHost(Long hostId) {
        return repository.findAllByHostId(hostId);
    }

    @Override
    public Accommodation get(Long accommodationId) {
        Optional<Accommodation> found = repository.findById(accommodationId);
        if (found.isEmpty()) {
            String value = bundle.getString("accommodation.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public Accommodation create(Accommodation accommodation) {
        try {
            AccommodationModification modification = new AccommodationModification();

            modification.setName(accommodation.getName());
            modification.setAccommodation(accommodation);
            modification.setStatus(AccommodationModificationStatus.Pending);
            modification.setAmenities(new HashSet<Amenity>(accommodation.getAmenities()));
            modification.setImages(accommodation.getImages());
            modification.setAvailablePeriods(accommodation.getAvailablePeriods());
            modification.setHost(accommodation.getHost());
            modification.setDescription(accommodation.getDescription());
            modification.setLocation(accommodation.getLocation());
            modification.setType(accommodation.getType());
            modification.setIsAutomaticallyAccepted(accommodation.getIsAutomaticallyAccepted());
            modification.setMaxGuests(accommodation.getMaxGuests());
            modification.setMinGuests(accommodation.getMinGuests());
            modification.setPricingPerPerson(accommodation.getIsPricingPerPerson());
            modification.setRequestDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            modification.setRequestType(AccommodationModificationType.New);

            repository.save(accommodation);
            repository.flush();

            modificationService.create(modification);
            return accommodation;
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
            StringBuilder sb = new StringBuilder(1000);
            for (ConstraintViolation<?> error : errors) {
                sb.append(error.getMessage()).append("\n");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
        }
    }

    @Override
    public Accommodation update(Accommodation accommodation) {
        try {
            get(accommodation.getId()); // this will throw AccommodationNotFoundException if accommodation is not found
            repository.save(accommodation);
            repository.flush();
            return accommodation;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = (RuntimeException) c;
            }
            if ((c != null) && (c instanceof ConstraintViolationException)) {
                ConstraintViolationException c2 = (ConstraintViolationException) c;
                Set<ConstraintViolation<?>> errors = c2.getConstraintViolations();
                StringBuilder sb = new StringBuilder(1000);
                for (ConstraintViolation<?> error : errors) {
                    sb.append(error.getMessage() + "\n");
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, sb.toString());
            }
            throw ex;
        }
    }

    @Override
    public Accommodation remove(Long accommodationId) {
        Accommodation found = get(accommodationId);
        modificationService.removeByAccommodationId(accommodationId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }


    public AccommodationFilter parseFilter(String city, String checkin, String checkout, Integer guestNum, Double minPrice, Double maxPrice, String propertyTypes, String amenities, Double minRating){
        AccommodationFilter filter = new AccommodationFilter();
        filter.setCity(city);
        filter.setCheckin(parseDate(checkin));
        filter.setCheckout(parseDate(checkout));
        filter.setGuestNum(guestNum);
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setAmenities(parseAmenities(amenities));
        filter.setTypes(parseAccommodationTypes(propertyTypes));
        filter.setMinRating(minRating);

        return filter;
    }

    public LocalDate parseDate(String date){
            DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                if (date != null) {
                    return LocalDate.parse(date, DATE_FORMATTER);
                }
            } catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
            }
            return null;
    }

    @Override
    public Accommodation addImage(Long imageId, Long accommodationId) {
        Image image = imageService.get(imageId);
        Accommodation accommodation = get(accommodationId);
        if(image == null || accommodation==null)return null;
        List<Image> images =  accommodation.getImages();
        images.add(image);
        accommodation.setImages(images);
        repository.save(accommodation);
        return accommodation;
    }

    @Override
    public Accommodation addPeriod(Long periodId, Long accommodationId) {
        AvailablePeriod period = periodService.get(periodId);
        Accommodation accommodation = get(accommodationId);
        if(period == null || accommodation==null)return null;
        List<AvailablePeriod> periods =  accommodation.getAvailablePeriods();
        if(findFirstOverlappingPeriodIndex(periods,period.getTimeSlot())!=-1)throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Period overlaps with already present periods");;
        periods.add(period);
        accommodation.setAvailablePeriods(periods);
        repository.save(accommodation);
        return accommodation;
    }

    @Override
    public Accommodation removePeriod(Long periodId, Long accommodationId) {
        AvailablePeriod period = periodService.get(periodId);
        Accommodation accommodation = get(accommodationId);
        if(period == null || accommodation==null)return null;
        List<AvailablePeriod> periods =  accommodation.getAvailablePeriods();
        periods.remove(period);
        accommodation.setAvailablePeriods(periods);
        modificationService.removePeriod(period, accommodationId);
        repository.save(accommodation);
        periodService.remove(periodId);
        return accommodation;
    }

    @Override
    public Accommodation updateEditingStatus(Long accommodationId, boolean isBeingEdited) {
        Accommodation accommodation = get(accommodationId);
        if (accommodation == null) {
            return accommodation;
        }
        accommodation.setIsBeingEdited(isBeingEdited);
        repository.save(accommodation);
        return accommodation;
    }

    @Override
    public Accommodation updateAutoAccept(Long accommodationId, Boolean isAutomaticallyAccepted) {
        Accommodation accommodation = get(accommodationId);
        if (accommodation == null) {
            return accommodation;
        }
        accommodation.setIsAutomaticallyAccepted(isAutomaticallyAccepted);
        repository.save(accommodation);
        return accommodation;
    }

    @Override
    public Accommodation updateCancellationDeadline(Long accommodationId, int value) {
        Accommodation accommodation = get(accommodationId);
        if (accommodation == null || value < 0) {
            return null;
        }
        accommodation.setCancellationDeadline(value);
        repository.save(accommodation);
        return accommodation;
    }

    //Amenity form => /filter?amenity=Wi-Fi,Swimming%20Pool,Parking
    private List<String> parseAmenities(String amenityString) {
        if (amenityString == null || amenityString.isEmpty()) {
            return null;
        }

        String decodedAmenities = URLDecoder.decode(amenityString, StandardCharsets.UTF_8);

        String[] amenityNames = decodedAmenities.split(",");

        List<String> amenities = new ArrayList<>();
        for (String amenityName : amenityNames) {
            amenities.add(amenityName);
        }

        return amenities;
    }

    //?type=Hotel,Resort
    private List<AccommodationType> parseAccommodationTypes(String typeString) {
        if (typeString == null || typeString.isEmpty()) {
            return null;
        }

        return Arrays.stream(typeString.split(","))
                .map(AccommodationType::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Accommodation> search(AccommodationFilter filter){
        List<Accommodation> filteredAccommodations = repository.findAccommodationsByFilter(filter);
        if (filter.getCheckin() != null && filter.getCheckout() != null){
            filteredAccommodations = filterByAvailability(filteredAccommodations, filter);

            if (filter.getMinPrice() != null || filter.getMaxPrice() != null){
                filteredAccommodations = filterByPrice(filteredAccommodations, filter);
            }
        }
        return filteredAccommodations;
    }

    private List<Accommodation> filterByPrice(List<Accommodation> accommodations, AccommodationFilter filter) {
        List<Accommodation> filteredAccommodations = new ArrayList<>();
        TimeSlot desiredPeriod = new TimeSlot(filter.getCheckin(), filter.getCheckout());

        for(Accommodation accommodation: accommodations){
            float totalPrice = getTotalPrice(accommodation, desiredPeriod);
            if(filter.getMinPrice() != null && filter.getMinPrice() > totalPrice){
                continue;
            }
            if(filter.getMaxPrice() != null && filter.getMaxPrice() < totalPrice){
                continue;
            }
            filteredAccommodations.add(accommodation);
        }

        return filteredAccommodations;
    }

    public float getTotalPrice(Accommodation accommodation, TimeSlot desiredPeriod) {
        List<AvailablePeriod> availablePeriods = repository.findAvailablePeriodsSortedByStartDate(accommodation.getId());
        float totalPrice = 0;

        int startIndex = findFirstOverlappingPeriodIndex(availablePeriods, desiredPeriod);
        int endIndex = findSuccessivePeriodIndex(availablePeriods, startIndex, desiredPeriod);

        for (int i = startIndex; i <= endIndex; i++) {
            long numberOfNights = availablePeriods.get(i).getTimeSlot().countOverlappingDays(desiredPeriod);

            if(i == endIndex){
                totalPrice += availablePeriods.get(i).getPricePerNight() * (numberOfNights-1);
            } else {
                totalPrice += availablePeriods.get(i).getPricePerNight() * numberOfNights;
            }

        }

        return totalPrice;
    }

    private int findFirstOverlappingPeriodIndex(List<AvailablePeriod> availablePeriods, TimeSlot desiredPeriod) {
        for (int i = 0; i < availablePeriods.size(); i++) {
            TimeSlot currentTimeSlot = availablePeriods.get(i).getTimeSlot();
            if (!desiredPeriod.getStartDate().isBefore(currentTimeSlot.getStartDate()) &&
                    currentTimeSlot.overlaps(desiredPeriod)) {
                return i;
            }
        }
        return -1;
    }

    private int findSuccessivePeriodIndex(List<AvailablePeriod> availablePeriods, int startIndex, TimeSlot desiredPeriod) {
        int endIndex = startIndex;

        while (endIndex < availablePeriods.size() - 1 &&
                desiredPeriod.getEndDate().isAfter(availablePeriods.get(endIndex).getTimeSlot().getEndDate())) {
            if (!availablePeriods.get(endIndex).getTimeSlot().isSuccessive(availablePeriods.get(endIndex + 1).getTimeSlot())) {
                break;
            }
            endIndex++;
        }

        return endIndex;
    }

    public List<Accommodation> filterByAvailability(List<Accommodation> accommodations, AccommodationFilter filter) {

        List<Accommodation> filteredAccommodations = new ArrayList<>();

        for (Accommodation accommodation : accommodations) {
            if (hasAvailability(accommodation, new TimeSlot(filter.getCheckin(),filter.getCheckout()))) {
                filteredAccommodations.add(accommodation);
            }
        }

        return filteredAccommodations;
    }

    private boolean hasAvailability(Accommodation accommodation, TimeSlot desiredPeriod) {
        List<AvailablePeriod> availablePeriods = repository.findAvailablePeriodsSortedByStartDate(accommodation.getId());

        // Find the first available period that overlaps with the desired period
        TimeSlot currentTimeSlot;
        int foundIndex = -1;
        for (int i = 0; i < availablePeriods.size(); i++) {
            currentTimeSlot = availablePeriods.get(i).getTimeSlot();
            if(!desiredPeriod.getStartDate().isBefore(currentTimeSlot.getStartDate()) &&
                    currentTimeSlot.overlaps(desiredPeriod)){
                foundIndex = i;
                break;
            }
        }

        if(foundIndex == -1){
            return false;
        }

        //If found check successive periods
        int currentIndex = foundIndex;

        while(currentIndex < availablePeriods.size()-1 && desiredPeriod.getEndDate().isAfter(availablePeriods.get(currentIndex).getTimeSlot().getEndDate())){
            if(!availablePeriods.get(currentIndex).getTimeSlot().isSuccessive(availablePeriods.get(currentIndex+1).getTimeSlot())){
                return false;
            }
            currentIndex++;
        }

        return true;
    }

    @Override
    public void removeAllByHost(Long hostId) {
        for(Accommodation accommodation : getAllByHost(hostId)) {
            accommodationReviewService.removeFromAccommodation(accommodation.getId());
            modificationService.removeByAccommodationId(accommodation.getId());
            repository.delete(accommodation);
            repository.flush();
        }
    }

}
