package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import utils.ConfigReader;

/**
 * Base class for all Page Objects.
 * Provides shared WebDriver helpers so concrete pages stay clean.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
        PageFactory.initElements(driver, this);
    }

    /** Waits until the element is visible, then returns it. */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.refreshed(
        		ExpectedConditions.visibilityOf(element)));
    }

    /** Waits until the element is clickable, then clicks it. */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.refreshed(
        		ExpectedConditions.elementToBeClickable(element))).click();
    }

    /** Clears a text field and types the supplied value. */
    protected void clearAndType(WebElement element, String value) {
        waitForVisibility(element);
        element.clear();
        element.sendKeys(value);
    }

    /** Scrolls the element into view using JavaScript. */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /** Returns the current page title. */
    public String getPageTitle() {
        return driver.getTitle();
    }
}
