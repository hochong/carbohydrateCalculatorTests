package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pages.CarbohydrateCalculatorPage;
import utils.DriverManager;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Cucumber step definitions for the Carbohydrate Calculator feature.
 *
 * Each step maps directly to a method on {@link CarbohydrateCalculatorPage},
 * keeping business logic out of the glue code.
 */
public class CarbohydrateCalculatorSteps {

    private WebDriver driver;
    private CarbohydrateCalculatorPage calculatorPage;

    // -------------------------------------------------------------------------
    // Hooks
    // -------------------------------------------------------------------------

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        calculatorPage = new CarbohydrateCalculatorPage(driver);
    }

    @After
    public void tearDown(Scenario scenario) {
        // Attach a screenshot to the report on failure
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot on failure");
        }
        DriverManager.quitDriver();
    }

    // -------------------------------------------------------------------------
    // Given steps
    // -------------------------------------------------------------------------

    @Given("the user opens the Carbohydrate Calculator page")
    public void theUserOpensTheCarbohydrateCalculatorPage() {
        calculatorPage.open();
        assertThat(calculatorPage.getPageTitle())
                .as("Page title should confirm the Carbohydrate Calculator loaded")
                .containsIgnoringCase("Carbohydrate Calculator");
    }

    @Given("the user selects {string}")
    public void theUserSelectsUnitTab(String unitTab) {
        switch (unitTab.toLowerCase()) {
            case "us units"     -> calculatorPage.selectUSUnits();
            case "metric units" -> calculatorPage.selectMetricUnits();
            case "other units"  -> calculatorPage.selectOtherUnits();
            default             -> throw new IllegalArgumentException("Unknown unit tab: " + unitTab);
        }
    }

    // -------------------------------------------------------------------------
    // When steps
    // -------------------------------------------------------------------------

    @When("the user enters age {string}")
    public void theUserEntersAge(String age) {
        calculatorPage.enterAge(age);
    }
    
    @When("the user clears the age field")
    public void theUserClearsTheAgeField() {
        calculatorPage.clearAgeField();
    }
    
    @When("the user loses focus on the age field")
    public void theUserLosesFocusOnTheAgeField() {
        calculatorPage.blurAgeField();
    }
    
    @When("the user selects gender {string}")
    public void theUserSelectsGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            calculatorPage.selectMale();
        } else {
            calculatorPage.selectFemale();
        }
    }

    @When("the user enters height of {string} feet and {string} inches")
    public void theUserEntersHeightInFeetAndInches(String feet, String inches) {
        calculatorPage.enterHeightUS(feet, inches);
    }

    @When("the user enters weight of {string} pounds")
    public void theUserEntersWeightInPounds(String pounds) {
        calculatorPage.enterWeightUS(pounds);
    }

    @When("the user enters height of {string} cm")
    public void theUserEntersHeightInCm(String cm) {
        calculatorPage.enterHeightMetric(cm);
    }

    @When("the user enters weight of {string} kg")
    public void theUserEntersWeightInKg(String kg) {
        calculatorPage.enterWeightMetric(kg);
    }

    @When("the user selects activity level {string}")
    public void theUserSelectsActivityLevel(String activityLevel) {
        calculatorPage.selectActivityLevel(activityLevel);
    }

    @When("the user clicks the Calculate button")
    public void theUserClicksTheCalculateButton() {
        calculatorPage.clickCalculate();
    }

    // -------------------------------------------------------------------------
    // Then steps
    // -------------------------------------------------------------------------

    @Then("{string} tab is selected")
    public void tabIsSelected(String tabName) {
    	assertThat(calculatorPage.getActiveUnitTab())
		    	.as(tabName + " tab should be selected")
		    	.containsIgnoringCase(tabName);
    }
    
    @Then("the result header should be displayed")
    public void theResultHeaderShouldBeDisplayed() {
        assertThat(calculatorPage.isResultHeaderDisplayed())
                .as("Result header should be visible after clicking Calculate")
                .isTrue();
    }
    
    @Then("the result suggestion should be displayed")
    public void theResultSuggestionShouldBeDisplayed() {
        assertThat(calculatorPage.isResultSuggestionDisplayed())
                .as("Result suggestion should be visible after clicking Calculate")
                .isTrue();
    }    
    
    @Then("the result section should be displayed")
    public void theResultSectionShouldBeDisplayed() {
        assertThat(calculatorPage.isResultDisplayed())
                .as("Result section should be visible after clicking Calculate")
                .isTrue();
    }

    @Then("the result section should be displayed:")
    public void theResultSectionShouldBeDisplayedWithTable(DataTable dataTable) {
        assertThat(calculatorPage.isResultDisplayed())
                .as("Result section should be visible after clicking Calculate")
                .isTrue();

        List<List<String>> expectedTable = dataTable.asLists();
        List<List<String>> actualTable   = calculatorPage.getResultTableData();

        assertThat(actualTable)
                .as("Result table rows and columns should match the expected data")
                .isEqualTo(expectedTable);
    }

    /**
     * Verifies the age error message element (#cageifcErr) using a DataTable.
     * Columns:
     *   text — expected text content of the error element (skipped if blank)
     *   css  — property::value pair to assert on the element (skipped if blank)
     */
    @Then("the age validation error message should be displayed:")
    public void theAgeValidationErrorMessageShouldBeDisplayed(DataTable dataTable) {
        assertThat(calculatorPage.isAgeErrorMessageDisplayed())
                .as("Age field validation error message should be displayed when field is empty and loses focus")
                .isTrue();

        Map<String, String> row = dataTable.asMaps().get(0);

        String expectedText = row.get("text");
        if (expectedText != null && !expectedText.isBlank()) {
            assertThat(calculatorPage.getAgeErrorMessage())
                    .as("Age error message text should contain: " + expectedText)
                    .containsIgnoringCase(expectedText);
        }

        String css = row.get("css");
        if (css != null && !css.isBlank()) {
            String[] parts = css.split("::", 2);
            assertThat(calculatorPage.getAgeErrorCssValue(parts[0]))
                    .as("Age error element CSS [" + parts[0] + "] should be " + parts[1])
                    .isEqualTo(parts[1]);
        }
    }

    /**
     * Verifies the age input field (#cage) using a DataTable.
     * Columns:
     *   text — reserved for future text assertion (skipped if blank)
     *   css  — property::value pair to assert on the field (skipped if blank)
     */
    @Then("age field should be displayed:")
    public void ageFieldShouldBeDisplayed(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps().get(0);

        String css = row.get("css");
        if (css != null && !css.isBlank()) {
            String[] parts = css.split("::", 2);
            assertThat(calculatorPage.getAgeFieldCssValue(parts[0]))
                    .as("Age field CSS [" + parts[0] + "] should be " + parts[1])
                    .isEqualTo(parts[1]);
        }
    }
    
    @Then("an error result should be shown")
    public void anErrorResultShouldBeShown() {
        assertThat(calculatorPage.isErrorResultDisplayed())
                .as("Error result should be displayed when required fields are missing")
                .isTrue();
    }
}
