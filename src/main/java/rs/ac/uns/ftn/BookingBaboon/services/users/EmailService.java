package rs.ac.uns.ftn.BookingBaboon.services.users;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.BookingBaboon.domain.users.User;
import rs.ac.uns.ftn.BookingBaboon.services.tokens.ITokenService;
import rs.ac.uns.ftn.BookingBaboon.services.users.interfaces.IEmailService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;


    private final ITokenService tokenService;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    @Override
    public void sendActivationEmail(User user) {
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:4200/users/activate?token=" + token;
        String message = "Please verify your email by clicking on the following link: ";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\r\n "+ confirmationUrl);
        javaMailSender.send(mailMessage);
    }
}
