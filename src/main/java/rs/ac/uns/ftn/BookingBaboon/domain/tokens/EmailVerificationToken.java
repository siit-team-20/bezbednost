package rs.ac.uns.ftn.BookingBaboon.domain.tokens;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@Table(name = "emailVerificationTokens")
@TableGenerator(name="email_verification_token_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="token", valueColumnName="value_pk")
public class EmailVerificationToken {

    private static final long EXPIRATION = 60 * 24 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "email_verification_token_id_generator")
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private Date expiryDate;

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public EmailVerificationToken(){}

    public EmailVerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = new Date (Calendar.getInstance().getTime().getTime() + EXPIRATION);
    }
}
