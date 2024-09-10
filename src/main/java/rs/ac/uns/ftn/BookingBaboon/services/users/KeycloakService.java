// package rs.ac.uns.ftn.BookingBaboon.services.users;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Service;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.client.RestClientException;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.*;
// import rs.ac.uns.ftn.BookingBaboon.dtos.users.UserCreationKeycloak;

// import java.util.Map;

// @Service
// public class KeycloakService {
//     @Value("${keycloak.base-url}")
//     private String baseURL;

//     @Value("${keycloak.realm}")
//     private String realm;

//     @Value("${keycloak.client-id}")
//     private String clientId;

//     @Value("${keycloak.username}")
//     private String username;

//     @Value("${keycloak.password}")
//     private String password;

//     public String obtainAccessToken() throws Exception, RestClientException {
//         String url = this.baseURL + "realms/master/protocol/openid-connect/token";
//         RestTemplate restTemplate = new RestTemplate();

//         LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//         formData.add("username", username);
//         formData.add("password", password);
//         formData.add("grant_type", "password");
//         formData.add("client_id", "admin-cli");

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

//         HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
//         ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

//         if (response.getStatusCode() != HttpStatus.OK) throw new Exception();
//         return (String) response.getBody().get("access_token");
//     }

//     /// true if registration successful
//     public boolean registerUser(UserCreationKeycloak user, String token) throws RestClientException {
//         String url = this.baseURL + "admin/realms/" + this.realm + "/users";
//         RestTemplate restTemplate = new RestTemplate();
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.setBearerAuth(token);

//         HttpEntity<UserCreationKeycloak> request = new HttpEntity<>(user, headers);
//         ResponseEntity<String> response;
//         try {
//             response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
//         } catch (Exception e) {
//             return false;
//         }

//         return response.getStatusCode() == HttpStatus.CREATED;
//     }
// }
