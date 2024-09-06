package rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Amenity;

@Repository
public interface IAmenityRepository extends JpaRepository<Amenity, Long> {

    public Amenity findByName(String name);
}
