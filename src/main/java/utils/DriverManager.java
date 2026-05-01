package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Manages the WebDriver lifecycle using the Singleton pattern.
 * Ensures a single driver instance per thread, supporting parallel execution.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static final int IMPLICIT_WAIT_SECONDS = 10;
    private static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    private DriverManager() {
        // Utility class — not instantiable
    }

    /**
     * Initialises and returns a Chrome WebDriver instance.
     * Subsequent calls on the same thread return the existing driver.
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            // Remove the next line to run with a visible browser window
            // options.addArguments("--headless=new");

            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));

            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }

    /**
     * Quits the driver and removes it from the ThreadLocal store.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
