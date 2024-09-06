package rs.ac.uns.ftn.BookingBaboon.e2e.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyAccommodationsPage {
    private WebDriver driver;
    private static String PAGE_URL="http://localhost:4200/host/accommodations";

    @FindBy(css = ".app-title")
    WebElement heading;

    @FindBy(css = "button[ng-reflect-router-link='/accommodations/periods/,9']")
    WebElement changeAvailability;

    public MyAccommodationsPage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(){
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(heading, "Booking Baboon"));

        return isOpened;
    }


    public void goToChangeAvailability(){
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(changeAvailability)).click();
    }

}
