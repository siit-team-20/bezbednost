package rs.ac.uns.ftn.BookingBaboon.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Host;

@Repository
public interface IHostRepository extends JpaRepository<Host, Long> {

    public Host findByEmail(String email);
}
