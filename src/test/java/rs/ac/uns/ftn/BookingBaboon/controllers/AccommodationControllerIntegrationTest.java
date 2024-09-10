// package rs.ac.uns.ftn.BookingBaboon.controllers;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.*;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.TestPropertySource;
// import rs.ac.uns.ftn.BookingBaboon.config.security.JwtTokenUtil;
// import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.Accommodation;
// import rs.ac.uns.ftn.BookingBaboon.dtos.accommodation_handling.accommodation.AccommodationResponse;
// import rs.ac.uns.ftn.BookingBaboon.dtos.reservation.ReservationCreateRequest;

// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// //@TestPropertySource(
// //        locations = "classpath:application-test.properties")
// @ActiveProfiles("test")
// public class AccommodationControllerIntegrationTest {
//     @LocalServerPort
//     private int port;

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Autowired
//     private JwtTokenUtil jwtTokenUtil;

//     @Test
//     @DisplayName("Should Get Accommodation by ID When making GET request to /api/v1/accommodations/{id}")
//     public void shouldRetrieveAccommodationById() {
//         Long accommodationId = 1L;

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId,
//                 HttpMethod.GET,
//                 null,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         AccommodationResponse accommodation = responseEntity.getBody();
//         assertNotNull(accommodation);
//         assertEquals(accommodation.getId(), accommodationId);
//     }

//     @Test
//     @DisplayName("Should Get NotFound status for accommodation with invalid ID When making GET request to /api/v1/accommodations/{id}")
//     public void shouldNotRetrieveAccommodationByInvalidId() {
//         Long invalidAccommodationId = -1L;

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + invalidAccommodationId,
//                 HttpMethod.GET,
//                 null,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

//         AccommodationResponse accommodation = responseEntity.getBody();
//         assertNull(accommodation.getId());
//     }

//     @Test
//     @DisplayName("Should Get Total Price When making GET request to /api/v1/accommodations/{id}/total-price")
//     public void shouldRetrieveTotalPrice() {
//         Long accommodationId = 1L;

//         String checkin = "2024-01-01";
//         String checkout = "2024-01-05";

//         ResponseEntity<Float> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/total-price" +
//                         "?checkin=" + checkin +
//                         "&checkout=" + checkout,
//                 HttpMethod.GET,
//                 null,
//                 Float.class);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         Float totalPrice = responseEntity.getBody();
//         assertNotNull(totalPrice);
//         assertEquals(totalPrice, 320L);
//     }

//     @Test
//     @DisplayName("Should add Available Period to Accommodation When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldAddAvailablePeriodToAccommodation() {
//         Long accommodationId = 1L;
//         Long periodId = 45L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/addPeriod/" + periodId,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         AccommodationResponse accommodation = responseEntity.getBody();
//         assertNotNull(accommodation);
//         assertEquals(accommodation.getId(), accommodationId);
//         assertTrue(accommodation.getAvailablePeriods().stream().anyMatch(availablePeriodResponse -> availablePeriodResponse.getId().equals(periodId)));
//     }

//     @Test
//     @DisplayName("Should return NOT ACCEPTABLE when trying to add overlapping Available Period to Accommodation When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldNotAddOverlappingAvailablePeriodToAccommodation() {
//         Long accommodationId = 1L;
//         Long periodId = 1L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/addPeriod/" + periodId,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to add Available Period with invalid id to Accommodation When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldNotAddInvalidAvailablePeriodToAccommodation() {
//         Long accommodationId = 1L;
//         Long periodId = 777L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/addPeriod/" + periodId,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to add Available Period to Accommodation with invalid id When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldNotAddAvailablePeriodToInvalidAccommodation() {
//         Long accommodationId = 777L;
//         Long periodId = 45L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/addPeriod/" + periodId,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should remove Available Period from Accommodation When making DELETE request to /api/v1/accommodations/{accommodationId}/available/{periodId}")
//     public void shouldRemoveAvailablePeriodFromAccommodation() {
//         Long accommodationId = 1L;
//         Long periodId = 44L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/available-periods/" + periodId,
//                 HttpMethod.DELETE,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         AccommodationResponse accommodation = responseEntity.getBody();
//         assertNotNull(accommodation);
//         assertEquals(accommodation.getId(), accommodationId);
//         assertTrue(accommodation.getAvailablePeriods().stream().noneMatch(availablePeriodResponse -> availablePeriodResponse.getId().equals(periodId)));
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to remove Available Period with invalid id from Accommodation When making DELETE request to /api/v1/accommodations/{accommodationId}/available/{periodId}")
//     public void shouldNotRemoveInvalidAvailablePeriodFromAccommodation() {
//         Long accommodationId = 1L;
//         Long periodId = 777L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/available-periods/" + periodId,
//                 HttpMethod.DELETE,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to remove Available Period from Accommodation with invalid id When making DELETE request to /api/v1/accommodations/{accommodationId}/available/{periodId}")
//     public void shouldNotRemoveAvailablePeriodFromInvalidAccommodation() {
//         Long accommodationId = 777L;
//         Long periodId = 44L;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/available-periods/" + periodId,
//                 HttpMethod.DELETE,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should update Cancellation Deadline for Accommodation When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldUpdateCancellationDeadlineToAccommodation() {
//         Long accommodationId = 1L;
//         int cancellationDeadline = 4;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/cancellation-deadline/" + cancellationDeadline,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         AccommodationResponse accommodation = responseEntity.getBody();
//         assertNotNull(accommodation);
//         assertEquals(accommodation.getId(), accommodationId);
//         assertEquals(accommodation.getCancellationDeadline(),cancellationDeadline);
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to update Cancellation Deadline for Accommodation with invalid id When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldNotUpdateCancellationDeadlineToInvalidAccommodation() {
//         Long accommodationId = 777L;
//         int cancellationDeadline = 4;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/cancellation-deadline/" + cancellationDeadline,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//     }

//     @Test
//     @DisplayName("Should return NOT FOUND when trying to update Cancellation Deadline with invalid value for Accommodation When making PUT request to /api/v1/accommodations/{accommodationId}/addPeriod/{periodId}")
//     public void shouldNotUpdateInvalidCancellationDeadlineToAccommodation() {
//         Long accommodationId = 1L;
//         int cancellationDeadline = -4;

//         String token = jwtTokenUtil.generateTokenForHost(1L, "john.doe@example.com");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setBearerAuth(token);

//         HttpEntity<String> requestEntity = new HttpEntity<>(headers);

//         ResponseEntity<AccommodationResponse> responseEntity = restTemplate.exchange(
//                 "http://localhost:" + port + "/api/v1/accommodations/" + accommodationId + "/cancellation-deadline/" + cancellationDeadline,
//                 HttpMethod.PUT,
//                 requestEntity,
//                 AccommodationResponse.class);

//         assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//     }
// }
