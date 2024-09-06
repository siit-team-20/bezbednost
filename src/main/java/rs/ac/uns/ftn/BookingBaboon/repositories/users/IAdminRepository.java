package rs.ac.uns.ftn.BookingBaboon.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.BookingBaboon.domain.users.Admin;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Long> {
}
