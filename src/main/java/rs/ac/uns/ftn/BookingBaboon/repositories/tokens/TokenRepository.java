package rs.ac.uns.ftn.BookingBaboon.repositories.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.BookingBaboon.domain.tokens.EmailVerificationToken;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

public interface TokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByToken(String token);

    EmailVerificationToken findByUser(User user);
}
