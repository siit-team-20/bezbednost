package rs.ac.uns.ftn.BookingBaboon.services.tokens;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.BookingBaboon.domain.tokens.EmailVerificationToken;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.repositories.tokens.TokenRepository;


@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{

    private final TokenRepository repository;

    @Override
    public void createVerificationToken(User user, String token) {
        EmailVerificationToken myToken = new EmailVerificationToken(token, user);
        repository.save(myToken);
    }

    @Override
    public EmailVerificationToken getVerificationToken(String verificationToken) {
        return repository.findByToken(verificationToken);
    }

    @Override
    public void delete(User user) {
        EmailVerificationToken token = repository.findByUser(user);
        if (token == null) return;
        repository.delete(token);
    }

    @Override
    public void delete(String token) {
        repository.delete(repository.findByToken(token));
    }


}
