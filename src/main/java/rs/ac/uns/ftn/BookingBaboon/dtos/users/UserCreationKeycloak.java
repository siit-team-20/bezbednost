package rs.ac.uns.ftn.BookingBaboon.dtos.users;

import lombok.Data;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.admins.AdminCreateRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.guests.GuestCreateRequest;
import rs.ac.uns.ftn.BookingBaboon.dtos.users.hosts.HostCreateRequest;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class UserCreationKeycloak {
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public HashMap<String, ArrayList<String>> attributes;
    public ArrayList<HashMap<String, String>> credentials;
    private static boolean enabled = true;

    public static UserCreationKeycloak fromUserCreateRequest(UserCreateRequest createRequest) {
        UserCreationKeycloak userCreationKeycloak = new UserCreationKeycloak();
        userCreationKeycloak.username = createRequest.getEmail();
        userCreationKeycloak.email = createRequest.getEmail();
        userCreationKeycloak.firstName = createRequest.getFirstName();
        userCreationKeycloak.lastName = createRequest.getLastName();

        userCreationKeycloak.credentials = new ArrayList<>();
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", createRequest.getPassword());
        userCreationKeycloak.credentials.add(credentials);

        ArrayList<String> title = new ArrayList<>();
        title.add("USER");
        ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add(createRequest.getPhoneNumber());
        ArrayList<String> address = new ArrayList<>();
        address.add(createRequest.getAddress());
        userCreationKeycloak.attributes = new HashMap<>();
        userCreationKeycloak.attributes.put("title", title);
        userCreationKeycloak.attributes.put("phoneNumber", phoneNumber);
        userCreationKeycloak.attributes.put("address", address);

        return userCreationKeycloak;
    }

    public static UserCreationKeycloak fromHostCreateRequest(HostCreateRequest createRequest) {
        UserCreationKeycloak userCreationKeycloak = new UserCreationKeycloak();
        userCreationKeycloak.username = createRequest.getEmail();
        userCreationKeycloak.email = createRequest.getEmail();
        userCreationKeycloak.firstName = createRequest.getFirstName();
        userCreationKeycloak.lastName = createRequest.getLastName();

        userCreationKeycloak.credentials = new ArrayList<>();
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", createRequest.getPassword());
        userCreationKeycloak.credentials.add(credentials);

        ArrayList<String> title = new ArrayList<>();
        title.add("HOST");
        ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add(createRequest.getPhoneNumber());
        ArrayList<String> address = new ArrayList<>();
        address.add(createRequest.getAddress());
        userCreationKeycloak.attributes = new HashMap<>();
        userCreationKeycloak.attributes.put("title", title);
        userCreationKeycloak.attributes.put("phoneNumber", phoneNumber);
        userCreationKeycloak.attributes.put("address", address);

        return userCreationKeycloak;
    }

    public static UserCreationKeycloak fromGuestCreateRequest(GuestCreateRequest createRequest) {
        UserCreationKeycloak userCreationKeycloak = new UserCreationKeycloak();
        userCreationKeycloak.username = createRequest.getEmail();
        userCreationKeycloak.email = createRequest.getEmail();
        userCreationKeycloak.firstName = createRequest.getFirstName();
        userCreationKeycloak.lastName = createRequest.getLastName();

        userCreationKeycloak.credentials = new ArrayList<>();
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", createRequest.getPassword());
        userCreationKeycloak.credentials.add(credentials);

        ArrayList<String> title = new ArrayList<>();
        title.add("GUEST");
        ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add(createRequest.getPhoneNumber());
        ArrayList<String> address = new ArrayList<>();
        address.add(createRequest.getAddress());
        userCreationKeycloak.attributes = new HashMap<>();
        userCreationKeycloak.attributes.put("title", title);
        userCreationKeycloak.attributes.put("phoneNumber", phoneNumber);
        userCreationKeycloak.attributes.put("address", address);

        return userCreationKeycloak;
    }

    public static UserCreationKeycloak fromAdminCreateRequest(AdminCreateRequest createRequest) {
        UserCreationKeycloak userCreationKeycloak = new UserCreationKeycloak();
        userCreationKeycloak.username = createRequest.getEmail();
        userCreationKeycloak.email = createRequest.getEmail();
        userCreationKeycloak.firstName = createRequest.getFirstName();
        userCreationKeycloak.lastName = createRequest.getLastName();

        userCreationKeycloak.credentials = new ArrayList<>();
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", createRequest.getPassword());
        userCreationKeycloak.credentials.add(credentials);

        ArrayList<String> title = new ArrayList<>();
        title.add("ADMIN");
        ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add(createRequest.getPhoneNumber());
        ArrayList<String> address = new ArrayList<>();
        address.add(createRequest.getAddress());
        userCreationKeycloak.attributes = new HashMap<>();
        userCreationKeycloak.attributes.put("title", title);
        userCreationKeycloak.attributes.put("phoneNumber", phoneNumber);
        userCreationKeycloak.attributes.put("address", address);

        return userCreationKeycloak;
    }
}
