package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * JUnit 4 entry point for Cucumber.
 *
 * Run the full suite via Maven:
 *   mvn clean test
 *
 * Run a single tag:
 *   mvn clean test -Dcucumber.filter.tags="@smoke"
 *
 * Reports are written to:
 *   target/cucumber-reports/cucumber.html
 *   target/cucumber-reports/cucumber.json
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        // Path to the feature files
		features = "classpath:features",

        // Package(s) that contain the step definitions and hooks
        glue = "steps",

        // Reporting plugins
        plugin = {
                "pretty",                                          // readable console output
                "html:target/cucumber-reports/cucumber.html",     // HTML report
                "json:target/cucumber-reports/cucumber.json",     // JSON for CI integration
                "junit:target/cucumber-reports/cucumber.xml"      // JUnit XML for CI
        },

        // Publish a live report (requires network; set to false for offline CI)
        publish = false,

        // Show every step in the console even when it passes
        monochrome = true
)
public class TestRunner {
    /*
     * This class intentionally left empty.
     * Cucumber drives execution via the annotations above.
     */
}
