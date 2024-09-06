package rs.ac.uns.ftn.BookingBaboon.e2e.tests;



import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.ComponentScan;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import rs.ac.uns.ftn.BookingBaboon.domain.accommodation_handling.AvailablePeriod;
import rs.ac.uns.ftn.BookingBaboon.domain.shared.TimeSlot;
import rs.ac.uns.ftn.BookingBaboon.e2e.pages.ChangeAvailabilityPage;
import rs.ac.uns.ftn.BookingBaboon.e2e.pages.HomePage;
import rs.ac.uns.ftn.BookingBaboon.e2e.pages.LoginPage;
import rs.ac.uns.ftn.BookingBaboon.e2e.pages.MyAccommodationsPage;
import rs.ac.uns.ftn.BookingBaboon.repositories.accommodation_handling.IAvailablePeriodRepository;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.AvailablePeriodService;
import rs.ac.uns.ftn.BookingBaboon.services.accommodation_handling.interfaces.IAvailablePeriodService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class ChangeAccommodationAvailabilityTest extends TestBase{

    private final String hostUsername = "john.doe@example.com";
    private final String hostPassword = "password123";

    private final Long accommodationId = 9L;

    private final String validStartDate = "January 30, 2024";

    private final String validEndDate = "January 31, 2024";

    private final String invalidStartDate = "January 1, 2024";

    private final String invalidEndDate = "January 2, 2024";

    private final String validEditStartDate = "May 24, 2024";

    private final String validEditEndDate = "May 25, 2024";

    private final String validPeriodText = "Jan 30, 2024 - Jan 31, 2024 | Price: €8.00";

    private final String validEditPeriodText = "May 21, 2024 - May 31, 2024 | Price: €75.00";

    private final String validEditedPeriodText = "May 24, 2024 - May 25, 2024 | Price: €6.00";

    private final String validRemovePeriodText = "Apr 21, 2024 - Apr 30, 2024 | Price: €90.00";

    private final String invalidDateText = "Jan 1, 2024 - Jan 2, 2024";

    private final String invalidPriceText = "Price: €-8.00";

    private final float validPrice = 8.0F;

    private final float invalidPrice = -1.0F;

    private final float validEditPrice = 6.0F;
    private final int cancellationDeadline = 6;

    private final int invalidCancellationDeadline = -1;

    private final String validYear = "2024";

    private final String validMonth = "May";

    private final String invalidYear = "2023";

    private final String invalidMonth = "October";

    private final String invalidEditStartDate = "October 1, 2023";

    private final String invalidEditEndDate = "October 2, 2023";

    @PersistenceContext
    EntityManager entityManager;

    public void rollbackDatabase(){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("rollback.sql");
        String rollbackScript = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        entityManager.createNativeQuery(rollbackScript).executeUpdate();
    }

    private void loginAsHost() {
        HomePage homePage = new HomePage(driver);
        if (!homePage.isLoggedIn()) {
            Assert.assertTrue(homePage.isPageOpened());
            homePage.goToLogin();
            LoginPage loginPage = new LoginPage(driver);
            Assert.assertTrue(loginPage.isPageOpened());
            loginPage.logIn(hostUsername, hostPassword);
        }
    }

    public ChangeAvailabilityPage navigateToChangeAccommodationAvailability(){
        loginAsHost();
        HomePage homePageHost = new HomePage(driver);
        Assert.assertTrue(homePageHost.isPageOpened());
        homePageHost.goToHostAccommodations();
        MyAccommodationsPage myAccommodationsPage = new MyAccommodationsPage(driver);
        Assert.assertTrue(myAccommodationsPage.isPageOpened());
        myAccommodationsPage.goToChangeAvailability();
        ChangeAvailabilityPage changeAvailabilityPage = new ChangeAvailabilityPage(driver,accommodationId);
        Assert.assertTrue(changeAvailabilityPage.isPageOpened());
        return changeAvailabilityPage;
    }

    @Test
    public void addAvailablePeriodTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.enterDates(validStartDate, validEndDate);
        changeAvailabilityPage.enterPrice(validPrice);
        changeAvailabilityPage.addPeriod();
        Assert.assertTrue(changeAvailabilityPage.isPeriodAdded(validPeriodText));
    }

    @Test
    public void editAvailablePeriodTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.selectEditPeriod(validEditPeriodText);
        changeAvailabilityPage.enterEditDates(validEditStartDate, validEditEndDate);
        changeAvailabilityPage.enterEditPrice(validEditPrice);
        changeAvailabilityPage.editPeriod();
        Assert.assertTrue(changeAvailabilityPage.isPeriodAdded(validEditedPeriodText));
    }

    @Test
    public void removeAvailablePeriodTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.selectRemovePeriod(validRemovePeriodText);
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(validRemovePeriodText));
    }

    @Test
    public void changeAvailabilityTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.enterDates(validStartDate, validEndDate);
        changeAvailabilityPage.enterPrice(validPrice);
        changeAvailabilityPage.addPeriod();
        Assert.assertTrue(changeAvailabilityPage.isPeriodAdded(validPeriodText));
        changeAvailabilityPage.selectEditPeriod(validEditPeriodText);
        changeAvailabilityPage.enterEditDates(validEditStartDate, validEditEndDate);
        changeAvailabilityPage.enterEditPrice(validEditPrice);
        changeAvailabilityPage.editPeriod();
        Assert.assertTrue(changeAvailabilityPage.isPeriodAdded(validEditedPeriodText));
        changeAvailabilityPage.selectRemovePeriod(validRemovePeriodText);
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(validRemovePeriodText));
        changeAvailabilityPage.enterCancellationDeadline(cancellationDeadline);
        changeAvailabilityPage.submitChanges();
        ChangeAvailabilityPage changeAvailabilityPage1 = navigateToChangeAccommodationAvailability();
        Assert.assertTrue(changeAvailabilityPage.isPeriodAdded(validPeriodText));
        Assert.assertTrue(changeAvailabilityPage1.isPeriodAdded(validEditedPeriodText));
        Assert.assertFalse(changeAvailabilityPage1.isPeriodAdded(validRemovePeriodText));
        Assert.assertTrue(changeAvailabilityPage.isCancellationDeadlineChanged(cancellationDeadline));
        rollbackDatabase();
    }

    @Test
    public void addInvalidAvailablePeriodTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.enterDates(invalidStartDate, invalidEndDate);
        changeAvailabilityPage.enterPrice(validPrice);
        changeAvailabilityPage.addPeriod();
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(invalidDateText));
    }

    @Test
    public void addInvalidPriceTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.enterDates(validStartDate, validEndDate);
        changeAvailabilityPage.enterPrice(invalidPrice);
        changeAvailabilityPage.addPeriod();
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(invalidDateText));
    }

    @Test
    public void addInvalidPeriodTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.enterDates(invalidStartDate, invalidEndDate);
        changeAvailabilityPage.enterPrice(invalidPrice);
        changeAvailabilityPage.addPeriod();
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(invalidDateText));
    }

    @Test
    public void editInvalidDateTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.selectEditPeriod(validEditPeriodText);
        changeAvailabilityPage.enterInvalidPeriod(invalidYear, invalidMonth ,invalidEditStartDate, invalidEditEndDate);
        changeAvailabilityPage.enterEditPrice(validEditPrice);
        changeAvailabilityPage.editPeriod();
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(validEditedPeriodText));
    }

    @Test
    public void editInvalidPriceTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.selectEditPeriod(validEditPeriodText);
        changeAvailabilityPage.enterInvalidPeriod(validYear, validMonth ,validEditStartDate, validEditEndDate);
        changeAvailabilityPage.enterEditPrice(invalidPrice);
        changeAvailabilityPage.editPeriod();
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(validEditedPeriodText));
    }

    @Test
    public void editInvalidPeriodTest(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.selectEditPeriod(validEditPeriodText);
        changeAvailabilityPage.enterInvalidPeriod(invalidYear, invalidMonth ,invalidEditStartDate, invalidEditEndDate);
        changeAvailabilityPage.enterEditPrice(invalidPrice);
        changeAvailabilityPage.editPeriod();
        Assert.assertFalse(changeAvailabilityPage.isPeriodAdded(validEditedPeriodText));
    }

    @Test
    public void editInvalidCancellationDate(){
        ChangeAvailabilityPage changeAvailabilityPage = navigateToChangeAccommodationAvailability();
        changeAvailabilityPage.enterCancellationDeadline(invalidCancellationDeadline);
        Assert.assertFalse(changeAvailabilityPage.isCancellationDeadlineValid());

    }

}
