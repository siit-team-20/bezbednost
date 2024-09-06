package rs.ac.uns.ftn.BookingBaboon.services;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.BookingBaboon.config.TestSecurityConfig;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAvailablePeriodRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.AvailablePeriodService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class AvailablePeriodServiceTest {

    @Autowired
    private AvailablePeriodService availablePeriodService;

    @MockBean
    private IAvailablePeriodRepository availablePeriodRepository;

    @Test
    public void testCreateValidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        assertEquals(availablePeriodService.create(availablePeriod),availablePeriod);
        verify(availablePeriodRepository,times(1)).save(availablePeriod);
    }

    @Test
    public void testCreateInvalidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        doThrow(new ConstraintViolationException("", new HashSet<>())).when(availablePeriodRepository).save(availablePeriod);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> availablePeriodService.create(availablePeriod));
        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), exception.getStatusCode().value());
        verify(availablePeriodRepository,times(1)).save(availablePeriod);
    }

    @Test
    public void testUpdateValidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        AvailablePeriod oldAvailablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 5)),100F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(oldAvailablePeriod));
        assertEquals(availablePeriodService.update(availablePeriod),availablePeriod);
        verify(availablePeriodRepository,times(1)).save(availablePeriod);
    }

    @Test
    public void testUpdateInvalidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        AvailablePeriod oldAvailablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 5)),100F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(oldAvailablePeriod));
        doThrow(new ConstraintViolationException("", new HashSet<>())).when(availablePeriodRepository).save(availablePeriod);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> availablePeriodService.update(availablePeriod));
        verify(availablePeriodRepository,times(1)).save(availablePeriod);
    }

    @Test
    public void testUpdateNotFoundAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> availablePeriodService.update(availablePeriod));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(availablePeriodRepository, never()).save(availablePeriod);
    }

    @Test
    public void testRemoveValidAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.of(availablePeriod));
        assertEquals(availablePeriodService.remove(1L), availablePeriod);
        verify(availablePeriodRepository, times(1)).delete(availablePeriod);
    }

    @Test
    public void testRemoveNotFoundAvailablePeriod(){
        AvailablePeriod availablePeriod = new AvailablePeriod(1L, new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),10F);
        when(availablePeriodRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> availablePeriodService.remove(1L));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode().value());
        verify(availablePeriodRepository, never()).delete(availablePeriod);
    }
}
