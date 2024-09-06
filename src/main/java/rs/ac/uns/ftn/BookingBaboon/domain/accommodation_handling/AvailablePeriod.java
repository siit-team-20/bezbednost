package rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;

@Entity
@Data
@Table(name = "available_period")
@TableGenerator(name="available_period_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="available_period",initialValue = 45, valueColumnName="value_pk")
public class AvailablePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "available_period_id_generator")
    private Long id;
    @Embedded
    private TimeSlot timeSlot;

    @Min(1)
    private Float PricePerNight;

    public AvailablePeriod() {
    }

    // Additional constructor
    public AvailablePeriod(TimeSlot timeSlot, Float pricePerNight) {
        this.timeSlot = timeSlot;
        this.PricePerNight = pricePerNight;
    }

    public AvailablePeriod(long id, TimeSlot timeSlot, Float pricePerNight) {
        this.id = id;
        this.timeSlot = timeSlot;
        this.PricePerNight = pricePerNight;
    }
}
