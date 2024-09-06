package rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period.AvailablePeriodCreateRequest;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAvailablePeriodRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAvailablePeriodService;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AvailablePeriodService implements IAvailablePeriodService {
    private final IAvailablePeriodRepository repository;
    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Override
    public HashSet<AvailablePeriod> getAll() {
        return new HashSet<AvailablePeriod>(repository.findAll());
    }

    @Override
    public AvailablePeriod get(Long availablePeriodId) {
        Optional<AvailablePeriod> found = repository.findById(availablePeriodId);
        if (found.isEmpty()) {
            String value = bundle.getString("availablePeriod.notFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public AvailablePeriod create(AvailablePeriod availablePeriod) {
        try {
            repository.save(availablePeriod);
            repository.flush();
            return availablePeriod;
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
    public AvailablePeriod update(AvailablePeriod availablePeriod) {
        try {
            get(availablePeriod.getId()); // this will throw AvailablePeriodNotFoundException if availablePeriod is not found
            repository.save(availablePeriod);
            repository.flush();
            return availablePeriod;
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
    public AvailablePeriod remove(Long availablePeriodId) {
        AvailablePeriod found = get(availablePeriodId);
        repository.delete(found);
        repository.flush();
        return found;
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
        repository.flush();
    }


    public List<AvailablePeriod> getOverlappingPeriods(TimeSlot desiredTimeSlot, List<AvailablePeriod> allPeriods){
        List<AvailablePeriod> overlappingPeriods = new ArrayList<>();

        for (AvailablePeriod period: allPeriods){
            if(period.getTimeSlot().overlaps(desiredTimeSlot)){
                overlappingPeriods.add(period);
            }
        }

        return overlappingPeriods;
    }

    @Override
    public List<AvailablePeriod> splitPeriods(TimeSlot reservationTimeSlot, List<AvailablePeriod> availablePeriods) {
        List<AvailablePeriod> splitPeriods = new ArrayList<>();

        AvailablePeriod firstAvailablePeriod = availablePeriods.get(0);
        AvailablePeriod lastAvailablePeriod = availablePeriods.get(0);

        // Iterate through the availablePeriods to find the first and last periods
        for (AvailablePeriod availablePeriod : availablePeriods) {
            if (availablePeriod.getTimeSlot().getStartDate().isBefore(firstAvailablePeriod.getTimeSlot().getStartDate())) {
                firstAvailablePeriod = availablePeriod;
            }

            if (availablePeriod.getTimeSlot().getEndDate().isAfter(lastAvailablePeriod.getTimeSlot().getEndDate())) {
                lastAvailablePeriod = availablePeriod;
            }
        }

        LocalDate reservationStartDate = reservationTimeSlot.getStartDate();
        LocalDate reservationEndDate = reservationTimeSlot.getEndDate();
        LocalDate availablePeriodStartDate = firstAvailablePeriod.getTimeSlot().getStartDate();
        LocalDate availablePeriodEndDate = lastAvailablePeriod.getTimeSlot().getEndDate();

        if (reservationStartDate.isAfter(availablePeriodStartDate) && reservationStartDate.isBefore(availablePeriodEndDate)) {
            splitPeriods.add(new AvailablePeriod(new TimeSlot(availablePeriodStartDate, reservationStartDate), firstAvailablePeriod.getPricePerNight()));
        }

        if (reservationEndDate.isAfter(availablePeriodStartDate) && reservationEndDate.isBefore(availablePeriodEndDate)) {
            splitPeriods.add(new AvailablePeriod(new TimeSlot(reservationEndDate, availablePeriodEndDate), lastAvailablePeriod.getPricePerNight()));
        }
        return splitPeriods;
    }
}
