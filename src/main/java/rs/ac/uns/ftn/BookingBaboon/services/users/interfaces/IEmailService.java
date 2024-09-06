package rs.ac.uns.ftn.BookingBaboon.services.users.interfaces;

import rs.ac.uns.ftn.BookingBaboon.domain.users.User;

public interface IEmailService {
    public void sendEmail(String to, String subject, String body);

    public void sendActivationEmail(User user);

}
