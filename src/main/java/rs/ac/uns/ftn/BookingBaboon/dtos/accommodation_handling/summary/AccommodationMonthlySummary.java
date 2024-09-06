package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class AccommodationMonthlySummary {
    private Long accommodationId;
    private TimeSlot timeSlot;
    Map<Month, Integer> reservationsData;
    Map<Month, Double> profitData;
    public AccommodationMonthlySummary() {
        reservationsData = new LinkedHashMap<>();
        profitData = new LinkedHashMap<>();
    }

}
