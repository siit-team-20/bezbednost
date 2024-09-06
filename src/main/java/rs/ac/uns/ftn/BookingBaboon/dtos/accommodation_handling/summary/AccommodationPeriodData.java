package rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.summary;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;

@Data
public class AccommodationPeriodData {
    private String accommodationName;
    private int reservationsCount;
    private double totalProfit;
}
