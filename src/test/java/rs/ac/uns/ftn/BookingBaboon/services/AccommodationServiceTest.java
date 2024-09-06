package rs.ac.uns.ftn.BookingBaboon.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAccommodationModificationRepository;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAccommodationRepository;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAvailablePeriodRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.AccommodationService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAccommodationService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AccommodationServiceTest {

    @Autowired
    private IAccommodationService accommodationService;

    @MockBean
    private IAccommodationRepository accommodationRepository;

    @MockBean
    private IAccommodationModificationRepository accommodationModificationRepository;

    @MockBean
    private IAvailablePeriodRepository availablePeriodRepository;

    @MockBean
    private ResourceBundle bundle;
    @Test
    public void testParseDateValidInput() {
        LocalDate result = accommodationService.parseDate("2024-01-01");

        assertEquals(LocalDate.of(2024, 1, 1), result);
    }

    @Test
    public void testParseDateNullInput() {
        LocalDate result = accommodationService.parseDate(null);

        assertNull(result);
    }

    @Test
    public void testParseDateInvalidInput() {
        when(bundle.getString("invalidDateFormat")).thenReturn("Invalid date format");

        assertThrows(ResponseStatusException.class, () -> accommodationService.parseDate("invalid-date"));
    }

    @Test
    public void testGetExistingAccommodation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);

        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));

        Accommodation result = accommodationService.get(accommodation.getId());

        assertEquals(accommodation, result);

        verify(accommodationRepository, times(1)).findById(accommodation.getId());
    }

    @Test
    public void testGetNonExistingAccommodation() {
        long nonExistingId = 999L;

        when(accommodationRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(bundle.getString("accommodation.notFound")).thenReturn("Accommodation not found");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accommodationService.get(nonExistingId));
        assertEquals("Accommodation not found", exception.getReason());

        verify(accommodationRepository, times(1)).findById(nonExistingId);
    }

    @Test
    public void testGetTotalPrice() {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);

        TimeSlot desiredPeriod = new TimeSlot(
                LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 8)
        );

        List<AvailablePeriod> availablePeriods = Arrays.asList(
                new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3)), 50F),
                new AvailablePeriod(2L, new TimeSlot(LocalDate.of(2024, 1, 4), LocalDate.of(2024, 1, 6)), 60F),
                new AvailablePeriod(3L, new TimeSlot(LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10)), 70F)
        );

        when(accommodationRepository.findAvailablePeriodsSortedByStartDate(accommodation.getId())).thenReturn(availablePeriods);

        float totalPrice = accommodationService.getTotalPrice(accommodation, desiredPeriod);

        assertEquals(350, totalPrice);

        verify(accommodationRepository, times(1)).findAvailablePeriodsSortedByStartDate(accommodation.getId());
    }

    @Test
    public void testAddValidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(availablePeriod));
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailablePeriods(new ArrayList<>());
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        assertEquals(accommodationService.addPeriod(1L,1L).getAvailablePeriods().get(0),availablePeriod);
        verify(accommodationRepository, times(1)).save(accommodation);
    }

    @Test
    public void testAddNotFoundAvailablePeriod(){
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.empty());
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailablePeriods(new ArrayList<>());
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accommodationService.addPeriod(1L,1L));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(accommodationRepository, never()).save(accommodation);
    }

    @Test
    public void testAddAvailablePeriodNotFoundAccommodation(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(availablePeriod));
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailablePeriods(new ArrayList<>());
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accommodationService.addPeriod(1L,1L));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(accommodationRepository, never()).save(accommodation);
    }

    @Test
    public void testRemoveValidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(availablePeriod));
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailablePeriods(new ArrayList<>());
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        assertEquals(accommodationService.removePeriod(1L,1L).getAvailablePeriods().size(),0);
        verify(accommodationRepository, times(1)).save(accommodation);
    }

    @Test
    public void testRemoveNotFoundAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.empty());
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailablePeriods(new ArrayList<>());
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accommodationService.addPeriod(1L,1L));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(accommodationRepository, never()).save(accommodation);
    }

    @Test
    public void testRemoveAvailablePeriodNotFoundAccommodation(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(availablePeriod));
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setAvailablePeriods(new ArrayList<>());
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accommodationService.addPeriod(1L,1L));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(accommodationRepository, never()).save(accommodation);
    }

    @Test
    public void testUpdateValidCancellationDeadline(){
        int deadline = 8;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        Accommodation updatedAccommodation = accommodationService.updateCancellationDeadline(1L,8);
        assertEquals(updatedAccommodation.getCancellationDeadline(),deadline);
        verify(accommodationRepository, times(1)).save(accommodation);
    }

    @Test
    public void testUpdateValidCancellationDeadlineNotFoundAccommodation(){
        int deadline = 8;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> accommodationService.updateCancellationDeadline(1L,deadline));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(accommodationRepository, never()).save(accommodation);
    }

    @Test
    public void testUpdateInvalidCancellationDeadline(){
        int deadline = -1;
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        when(accommodationRepository.findById(accommodation.getId())).thenReturn(Optional.of(accommodation));
        Accommodation updatedAccommodation = accommodationService.updateCancellationDeadline(1L,deadline);
        assertNull(updatedAccommodation);
        verify(accommodationRepository, never()).save(accommodation);
    }
}
