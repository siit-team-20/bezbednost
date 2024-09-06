package rs.ac.uns.ftn.BookingBaboon.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.Console;
import java.time.Duration;
import java.util.List;

public class ChangeAvailabilityPage {
    private WebDriver driver;
    private static String PAGE_URL="http://localhost:4200/accommodations/periods/";

    @FindBy(css = ".app-title")
    WebElement heading;

    @FindBy(css = "mat-form-field#datepicker button[aria-label='Open calendar']")
    WebElement datepickerIcon;

    @FindBy(css="input[formcontrolname='price']")
    WebElement pricePerNightInput;

    @FindBy(id="add")
    WebElement addPeriodButton;

    @FindBy(css = "mat-calendar")
    WebElement calendar;

    @FindBy(id="row")
    List<WebElement> periods;

    @FindBy(css = "mat-form-field#datepicker1 button[aria-label='Open calendar']")
    WebElement editDatepicker;

    @FindBy(css = "mat-calendar")
    WebElement editCalendar;

    @FindBy(id="mat-input-2")
    WebElement editPricePerNightInput;

    @FindBy(id="edit")
    WebElement editButton;

    @FindBy(css = "input[formcontrolname='cancelDeadline']")
    WebElement cancelDeadlineInput;

    @FindBy(id = "submit")
    WebElement submitButton;

    @FindBy(css = "button.mat-calendar-period-button")
    WebElement periodButton;



    public ChangeAvailabilityPage(WebDriver driver, Long id){
        this.driver=driver;
        driver.get(PAGE_URL + id);

        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(){
        boolean isOpened = (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(heading, "Booking Baboon"));

        return isOpened;
    }

    public void enterDates(String startDate, String endDate){
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(datepickerIcon)).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(calendar));
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(calendar.findElement(new By.ByCssSelector("[aria-label='"+startDate+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(calendar.findElement(new By.ByCssSelector("[aria-label='"+endDate+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(calendar));
    }

    public void enterPrice(float price){
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(pricePerNightInput)).sendKeys(Float.toString(price));
    }

    public void addPeriod(){
        addPeriodButton.click();
    }

    public boolean isPeriodAdded(String periodText){
        for (WebElement period: periods){
            if(period.getText().contains(periodText))return true;
        }
        return false;
    }

    public void selectEditPeriod(String periodText){
        for (WebElement period: periods){
            if(period.getText().contains(periodText)){
                new WebDriverWait(driver, Duration.ofSeconds(1))
                        .until(ExpectedConditions.visibilityOf(period.findElement(new By.ByCssSelector("#icon")))).click();
                new WebDriverWait(driver, Duration.ofSeconds(1))
                        .until(ExpectedConditions.elementToBeClickable(editDatepicker)).click();
                new WebDriverWait(driver, Duration.ofSeconds(1))
                        .until(ExpectedConditions.visibilityOf(editCalendar));
            }
        }
    }

    public void enterEditDates(String startDate, String endDate ) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(editCalendar.findElement(new By.ByCssSelector("[aria-label='"+startDate+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(editCalendar.findElement(new By.ByCssSelector("[aria-label='"+endDate+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(editCalendar));
    }

    public void enterEditPrice(float price) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(editPricePerNightInput)).clear();
        editPricePerNightInput.sendKeys(Float.toString(price));
    }

    public void editPeriod() {
        editButton.click();
    }

    public void selectRemovePeriod(String periodText){
        for (WebElement period: periods){
            if(period.getText().contains(periodText)){
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.visibilityOf(period.findElement(new By.ByCssSelector("button")))).click();
            }
        }
    }

    public void enterCancellationDeadline(int days){
        //new WebDriverWait(driver, Duration.ofSeconds(10))
        //      .until(ExpectedConditions.visibilityOf(cancelDeadlineInput)).click();
        cancelDeadlineInput.sendKeys(Keys.CONTROL + "a");
        cancelDeadlineInput.sendKeys(Keys.DELETE);
        cancelDeadlineInput.sendKeys(Integer.toString(days));
    }

    public void submitChanges(){
        submitButton.click();
    }

    public void enterInvalidPeriod(String year, String month, String startDate, String endDate){
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(periodButton)).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(editCalendar.findElement(new By.ByCssSelector("[aria-label='"+year+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(editCalendar.findElement(new By.ByCssSelector("[aria-label='"+month+" "+year+ "']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(editCalendar.findElement(new By.ByCssSelector("[aria-label='"+startDate+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(editCalendar.findElement(new By.ByCssSelector("[aria-label='"+endDate+"']")))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(editCalendar));
    }

    public void refreshPage(Long id) {
        pricePerNightInput.sendKeys(Keys.F5);
    }

    public boolean isCancellationDeadlineChanged(int days) {
        String cancellationDeadline = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(cancelDeadlineInput)).getAttribute("value");
        return cancellationDeadline.equals(Integer.toString(days));
    }

    public boolean isCancellationDeadlineValid() {
        heading.click();
        return driver.findElements(new By.ByCssSelector("#cancelDeadline>.mdc-text-field--invalid")).isEmpty();
    }

}
