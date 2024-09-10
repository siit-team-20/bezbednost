package rs.ac.uns.ftn.BookingBaboon;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingBaboonApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingBaboonApplication.class, args);
		Security.addProvider(new BouncyCastleProvider());
	}

}