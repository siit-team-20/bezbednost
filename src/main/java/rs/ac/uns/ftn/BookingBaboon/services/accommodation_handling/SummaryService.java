package rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.reservation.Reservation;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary.AccommodationMonthlySummary;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary.AccommodationPeriodData;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary.MonthlyData;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary.PeriodSummary;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.ISummaryService;
import rs.ac.uns.ftn.BookingBaboon.services.reservation.interfaces.IReservationService;

import java.sql.Time;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SummaryService implements ISummaryService {
    private final IReservationService reservationService;
    private final IAccommodationService accommodationService;
    @Override
    public PeriodSummary getPeriodSummary(Long hostId, String startDate, String endDate) {
        PeriodSummary summary = new PeriodSummary();

        LocalDate periodStart = parseDate(startDate);
        LocalDate periodEnd = parseDate(endDate);
        TimeSlot period = new TimeSlot(periodStart,periodEnd);
        period.fix();

        List<AccommodationPeriodData> accommodationsData = new ArrayList<>();
        Set<Accommodation> accommodations = accommodationService.getAllByHost(hostId);

        for(Accommodation accommodation: accommodations){
            AccommodationPeriodData data = new AccommodationPeriodData();

            Collection<Reservation> reservations = reservationService.getAllFinishedByAccommodationAndTimeSlot(accommodation.getId(), period);

            data.setAccommodationName(accommodation.getName());
            data.setReservationsCount(reservations.size());
            data.setTotalProfit(reservations.stream().mapToDouble(Reservation::getPrice).sum());
            accommodationsData.add(data);
        }

        summary.setPeriod(period);
        summary.setAccommodationsData(accommodationsData);
        return summary;
    }

    @Override
    public AccommodationMonthlySummary getMonthlySummary(Long id) {
        AccommodationMonthlySummary summary = new AccommodationMonthlySummary();

        LocalDate currentDate = LocalDate.now();
        LocalDate dateOneYearAgo = currentDate.minusYears(1);
        Map<Month, Integer> reservationsData = new LinkedHashMap<>();
        Map<Month, Double> profitData = new LinkedHashMap<>();

        for (LocalDate date = dateOneYearAgo; date.isBefore(currentDate); date = date.plusMonths(1)) {
            int year = date.getYear();
            Month month = date.getMonth();

            Collection<Reservation> reservations = reservationService.getAllFinishedByAccommodationAndMonth(id, year, month.getValue());

            int reservationsCount = reservations.size();
            double totalProfit = reservations.stream().mapToDouble(Reservation::getPrice).sum();

            reservationsData.put(month, reservationsCount);
            profitData.put(month, totalProfit);
        }

        summary.setAccommodationId(id);
        summary.setTimeSlot(new TimeSlot(dateOneYearAgo, currentDate));
        summary.setReservationsData(reservationsData);
        summary.setProfitData(profitData);
        return summary;
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
}
