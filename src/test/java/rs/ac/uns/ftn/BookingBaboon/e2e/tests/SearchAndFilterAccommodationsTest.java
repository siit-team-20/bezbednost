package rs.ac.uns.ftn.BookingBaboon.e2e.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import rs.ac.uns.ftn.BookingBaboon.e2e.pages.HomePage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchAndFilterAccommodationsTest extends TestBase{
    static final String City = "Suburbia";
    static final String NonExistentCity = "aaaaaa";
    static final String Checkin = "April 3, 2024";
    static final String Checkout = "April 6, 2024";
    static final Integer GuestNum = 2;
    static final Integer MinPrice = 100;
    static final Integer MaxPrice = 500;
    static final Integer MinRating = 4;
    static final List<String> Amenities = new ArrayList<>(Arrays.asList("Wi-Fi", "Balcony"));
    static final List<String> AccommodationTypes = new ArrayList<>(List.of("Room"));

    @Test
    public void SearchAndFilterAccommodations() {
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isPageOpened());

        home.enterCity(City);
        home.enterPeriod(Checkin, Checkout);
        home.enterGuestNum(GuestNum);
        home.openFiltersBox();
        home.enterMinPrice(MinPrice);
        home.enterMaxPrice(MaxPrice);
        home.enterMinRating(MinRating);
        home.enterAccommodationTypes(AccommodationTypes);
        home.enterAmenities(Amenities);
        home.applyFilter();
        Assert.assertTrue(home.verifyResultsCity(City));
        Assert.assertTrue(home.verifyResultsRating(MinRating));
    }

    @Test
    public void SearchAccommodations() {
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isPageOpened());

        home.enterCity(City);
        home.enterPeriod(Checkin, Checkout);
        home.enterGuestNum(GuestNum);
        home.pressSearch();
        Assert.assertTrue(home.verifyResultsCity(City));
    }

    @Test
    public void SearchAccommodationsWithNoResults() {
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isPageOpened());

        home.enterCity(NonExistentCity);
        home.enterPeriod(Checkin, Checkout);
        home.enterGuestNum(GuestNum);
        home.pressSearch();
        Assert.assertFalse(home.accommodationsExist());
    }
}
