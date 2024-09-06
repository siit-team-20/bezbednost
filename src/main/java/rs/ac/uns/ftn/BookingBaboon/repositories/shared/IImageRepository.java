package rs.ac.uns.ftn.BookingBaboon.repositories.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.Image;

public interface IImageRepository extends JpaRepository<Image, Long> {
}
