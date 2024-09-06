package rs.ac.uns.ftn.BookingBaboon.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.notifications.NotificationType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "guests")
public class Guest extends User {

    @ManyToMany
    private Set<Accommodation> favorites = new HashSet<Accommodation>();

    public void addFavorite(Accommodation accommodation){
        favorites.add(accommodation);
    }

    public void removeFavorite(Accommodation accommodation){
        favorites.remove(accommodation);
    }
}
