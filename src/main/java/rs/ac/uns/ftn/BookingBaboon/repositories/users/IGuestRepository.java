package rs.ac.uns.ftn.BookingBaboon.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Guest;

@Repository
public interface IGuestRepository extends JpaRepository<Guest, Long> {
    public Guest findByEmail(String email);
}
