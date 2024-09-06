package rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationModification;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AccommodationModificationStatus;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAccommodationModificationRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationModificationService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAvailablePeriodService;
import rs.ac.uns.ftn.BookingBaboon.services.shared.IImageService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AccommodationModificationService implements IAccommodationModificationService {
    private final IAccommodationModificationRepository repository;
    private final IImageService imageService;
    private final IAvailablePeriodService periodService;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public HashSet<AccommodationModification> getAll() {
        return new HashSet<AccommodationModification>(repository.findAll());
    }

    @Override
    public AccommodationModification get(Long requestId) {
        Optional<AccommodationModification> found = repository.findById(requestId);
        if (found.isEmpty()) {
            String value = bundle.getString("accommodationChangeRequest.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public AccommodationModification create(AccommodationModification accommodationModification) {
        try {
            repository.save(accommodationModification);
            repository.flush();
            return accommodationModification;
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
    public AccommodationModification update(AccommodationModification reservation) {
        try {
            get(reservation.getId()); // this will throw AccommodationChangeRequestNotFoundException if reservation is not found
            repository.save(reservation);
            repository.flush();
            return reservation;
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
    public AccommodationModification remove(Long requestId) {
        AccommodationModification found = get(requestId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }

    @Override
    public AccommodationModification approve(Long id) {
        AccommodationModification accommodationModification = get(id);

        accommodationModification.setStatus(AccommodationModificationStatus.Approved);
        repository.save(accommodationModification);
        repository.flush();
        return accommodationModification;
    }

    @Override
    public AccommodationModification deny(Long id) {
        AccommodationModification accommodationModification = get(id);

        accommodationModification.setStatus(AccommodationModificationStatus.Denied);
        repository.save(accommodationModification);
        repository.flush();
        return accommodationModification;
    }

    @Override
    public void removePeriod(AvailablePeriod period, Long accommodationId) {
        Collection<AccommodationModification> modifications = getByAccommodationId(accommodationId);
        for (AccommodationModification modification: modifications) {
            List<AvailablePeriod> periods =  modification.getAvailablePeriods();
            if (periods.contains(period)) {
                periods.remove(period);
                modification.setAvailablePeriods(periods);
                repository.save(modification);
            }
        }

    }

    private Collection<AccommodationModification> getByAccommodationId(Long accommodationId) {
        return repository.findAllByAccommodationId(accommodationId);
    }

    @Override
    public AccommodationModification addImage(Long imageId, Long accommodationModificationId) {
        Image image = imageService.get(imageId);
        AccommodationModification accommodationModification = get(accommodationModificationId);
        if(image == null || accommodationModification==null)return null;
        List<Image> images =  accommodationModification.getImages();
        images.add(image);
        accommodationModification.setImages(images);
        repository.save(accommodationModification);
        return accommodationModification;
    }

    @Override
    public AccommodationModification addPeriod(Long periodId, Long accommodationModificationId) {
        AvailablePeriod period = periodService.get(periodId);
        AccommodationModification accommodationModification = get(accommodationModificationId);
        if(period == null || accommodationModification==null)return null;
        List<AvailablePeriod> periods =  accommodationModification.getAvailablePeriods();
        periods.add(period);
        accommodationModification.setAvailablePeriods(periods);
        repository.save(accommodationModification);
        return accommodationModification;
    }

    @Override
    public void removeByAccommodationId(Long accommodationId) {
        Collection<AccommodationModification> accommodationModifications = getByAccommodationId(accommodationId);

        for(AccommodationModification accommodationModification : accommodationModifications) {
            remove(accommodationModification.getId());
        }
    }


}
