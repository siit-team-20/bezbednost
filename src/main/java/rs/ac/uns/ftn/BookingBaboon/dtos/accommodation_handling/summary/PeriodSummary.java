package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class PeriodSummary {
    private TimeSlot period;
    private List<AccommodationPeriodData> accommodationsData;

    public PeriodSummary() {
        accommodationsData = new ArrayList<>();
    }
}
