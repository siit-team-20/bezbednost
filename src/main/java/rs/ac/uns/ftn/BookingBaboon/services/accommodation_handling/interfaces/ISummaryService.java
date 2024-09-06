package rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces;

import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary.AccommodationMonthlySummary;
import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary.PeriodSummary;

import java.util.Date;

public interface ISummaryService {
    PeriodSummary getPeriodSummary(Long hostId, String startDate, String endDate);
    AccommodationMonthlySummary getMonthlySummary(Long id);
}
