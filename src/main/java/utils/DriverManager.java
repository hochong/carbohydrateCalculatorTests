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
            // Required when Chrome runs inside a Docker container
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            if (ConfigReader.getBoolean("headless")) {
                options.addArguments("--headless=new", "--window-size=1920,1080");
            }

            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds")));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds")));

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
