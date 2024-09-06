package rs.ac.uns.ftn.BookingBaboon.services.tokens;

import rs.ac.uns.ftn.BookingBaboon.domain.tokens.EmailVerificationToken;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

public interface ITokenService {

    void createVerificationToken(User user, String token);
    EmailVerificationToken getVerificationToken(String verificationToken);

    void delete(User user);

    void delete(String token);
}
