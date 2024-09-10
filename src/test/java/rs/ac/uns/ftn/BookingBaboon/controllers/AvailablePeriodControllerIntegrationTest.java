// package rs.ac.uns.ftn.BookingBaboon.controllers;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.http.*;
// import org.springframework.test.context.ActiveProfiles;
// import rs.ac.uns.ftn.BookingBaboon.config.security.JwtTokenUtil;
// import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
// import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationResponse;
// import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period.AvailablePeriodCreateRequest;
// import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period.AvailablePeriodRequest;
// import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.available_period.AvailablePeriodResponse;
// import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestReference;

// import java.time.LocalDate;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("test")
// public class AvailablePeriodControllerIntegrationTest {
//     @LocalServerPort
//     private int port;

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Autowired
//     private JwtTokenUtil jwtTokenUtil;

//     @Test
//     @DisplayName("Should add Available Period When making POST request to /api/v1/available-periods")
//     public void shouldAddAvailablePeriod() {
//         AvailablePeriodCreateRequest request = new AvailablePeriodCreateRequest(
//                 new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),
//                 100F
//         );

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<AvailablePeriodCreateRequest> requestEntity = new HttpEntity<>(request,headers);

//         ResponseEntity<AvailablePeriodResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/available-periods",
//                 HttpMethod.POST,
//                 requestEntity,
//                 AvailablePeriodResponse.class);

//         assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

//         AvailablePeriodResponse response = responseEntity.getBody();
//         assertNotNull(response);
//     }

//     @Test
//     @DisplayName("Should update Available Period When making PUT request to /api/v1/available-periods")
//     public void shouldUpdateAvailablePeriod() {
//         AvailablePeriodRequest request = new AvailablePeriodRequest(1L,
//                 new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),
//                 100F
//         );

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<AvailablePeriodRequest> requestEntity = new HttpEntity<>(request,headers);

//         ResponseEntity<AvailablePeriodResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/available-periods",
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AvailablePeriodResponse.class);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         AvailablePeriodResponse response = responseEntity.getBody();
//         assertNotNull(response);
//         assertEquals(request.getPricePerNight(),response.getPricePerNight());
//         assertEquals(request.getTimeSlot(),response.getTimeSlot());
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to update invalid Available Period When making PUT request to /api/v1/available-periods")
//     public void shouldNotUpdateInvalidAvailablePeriod() {
//         AvailablePeriodRequest request = new AvailablePeriodRequest(777L,
//                 new TimeSlot(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5)),
//                 100F
//         );

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<AvailablePeriodRequest> requestEntity = new HttpEntity<>(request,headers);

//         ResponseEntity<AvailablePeriodResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/available-periods",
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AvailablePeriodResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }
// }
