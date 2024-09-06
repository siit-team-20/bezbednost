package rs.ac.uns.ftn.BookingBaboon.domain.reviews;

import jakarta.persistence.*;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Table(name = "reviews")
@TableGenerator(name="review_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="review", initialValue = 15, valueColumnName="value_pk")

public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "review_id_generator")
    private Long id;

    @ManyToOne
    private User reviewer;
    private Date createdOn;
    private short rating;
    private String comment;

    @Enumerated
    private ReviewStatus status = ReviewStatus.Pending;
}
