package rs.ac.uns.ftn.BookingBaboon.domain.shared;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Embeddable
@Data
public class TimeSlot {
    private LocalDate startDate;
    private LocalDate endDate;

    public TimeSlot(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimeSlot() {

    }

    public boolean overlaps(TimeSlot other) {
        return this.startDate.isBefore(other.endDate) && this.endDate.isAfter(other.startDate);
    }

    public long getNumberOfDays() {
        return endDate.toEpochDay() - startDate.toEpochDay();
    }

    public boolean isSuccessive(TimeSlot other) {
        // Check if the second time slot starts immediately after the first one ends
        return this.getEndDate().plusDays(1).equals(other.getStartDate());
    }

    public long countOverlappingDays(TimeSlot other) {
        // Find the maximum of the start dates and the minimum of the end dates
        LocalDate overlapStartDate = this.getStartDate().isAfter(other.getStartDate()) ?
                this.getStartDate() : other.getStartDate();

        LocalDate overlapEndDate = this.getEndDate().isBefore(other.getEndDate()) ?
                this.getEndDate() : other.getEndDate();

        // Calculate the number of overlapping days
        long overlappingDays = ChronoUnit.DAYS.between(overlapStartDate, overlapEndDate.plusDays(1));

        // Return the result
        return Math.max(0, overlappingDays); // Ensure non-negative result
    }

    public void fix(){
        startDate = startDate.plusDays(1);
        endDate = endDate.plusDays(1);
    }
}
