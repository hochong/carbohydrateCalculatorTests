package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import utils.ConfigReader;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for https://www.calculator.net/carbohydrate-calculator.html
 */
public class CarbohydrateCalculatorPage extends BasePage {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------
    public static final String PAGE_URL = ConfigReader.get("base.url");

    // -------------------------------------------------------------------------
    // TitleHeader
    // -------------------------------------------------------------------------
    @FindBy(xpath = "//h1[normalize-space()='Carbohydrate Calculator']")
    private WebElement titleHeader;

    // -------------------------------------------------------------------------
    // Unit tab links
    // -------------------------------------------------------------------------
    @FindBy(xpath = "//a[normalize-space()='US Units']")
    private WebElement usUnitsTab;

    @FindBy(xpath = "//a[normalize-space()='Metric Units']")
    private WebElement metricUnitsTab;

    @FindBy(xpath = "//a[normalize-space()='Other Units']")
    private WebElement otherUnitsTab;

    // -------------------------------------------------------------------------
    // Shared / US fields
    // -------------------------------------------------------------------------
    /** Age field (visible for all unit types). */
    @FindBy(id = "cage")
    private WebElement ageField;
    
    @FindBy(id = "cageifcErr")
    private WebElement ageFieldError;

    /** Male gender radio. */
    @FindBy(id = "csex1")
    private WebElement maleRadio;
    
    @FindBy(xpath = "//input[@id='csex1']/following-sibling::span")
    private WebElement maleRadioButton;

    /** Female gender radio. */
    @FindBy(id = "csex2")
    private WebElement femaleRadio;
    
    @FindBy(xpath = "//input[@id='csex2']/following-sibling::span")
    private WebElement femaleRadioButton;

    // -------------------------------------------------------------------------
    // US-unit height / weight fields
    // -------------------------------------------------------------------------
    @FindBy(id = "cheightfeet")
    private WebElement heightFeetField;

    @FindBy(id = "cheightinch")
    private WebElement heightInchesField;

    @FindBy(id = "cpound")
    private WebElement weightPoundsField;

    // -------------------------------------------------------------------------
    // Metric height / weight fields
    // -------------------------------------------------------------------------
    @FindBy(id = "cheightmeter")
    private WebElement heightCmField;

    @FindBy(id = "ckg")
    private WebElement weightKgField;

    // -------------------------------------------------------------------------
    // Activity level select (shared)
    // -------------------------------------------------------------------------
    @FindBy(id = "cactivity")
    private WebElement activityDropdown;

    // -------------------------------------------------------------------------
    // Settings toggle + BMR radio buttons
    // -------------------------------------------------------------------------
    @FindBy(xpath = "//a[contains(text(),'Settings')]")
    private WebElement settingsToggle;

    @FindBy(id = "cformula1")
    private WebElement bmrMifflinRadio;

    @FindBy(id = "cformula2")
    private WebElement bmrKatchRadio;

    // -------------------------------------------------------------------------
    // Calculate button
    // -------------------------------------------------------------------------
    @FindBy(xpath = "//input[@value='Calculate']")
    private WebElement calculateButton;

    // -------------------------------------------------------------------------
    // Result / error elements
    // -------------------------------------------------------------------------
    // The main result section that appears after a successful calculation
    @FindBy(xpath = "//h2[contains(@class,'h2result')]")
	private WebElement resultHeader;
    
    @FindBy(xpath = "//p[contains(@class,'bigtext')]")
	private WebElement resultSuggestion;
    
    @FindBy(xpath = "//table[contains(@class,'cinfoT')]")
	private WebElement resultSection;

    /** Error message shown when inputs are invalid. */
    @FindBy(xpath = "//*[contains(@style,'error.svg')]/div/font[contains(@color,'red')]")
    private WebElement errorMessage;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    public CarbohydrateCalculatorPage(WebDriver driver) {
        super(driver);
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------
    public CarbohydrateCalculatorPage open() {
        driver.get(PAGE_URL);
        return this;
    }

    // -------------------------------------------------------------------------
    // Unit tab actions
    // -------------------------------------------------------------------------
    public CarbohydrateCalculatorPage selectUSUnits() {
        waitAndClick(usUnitsTab);
        return this;
    }

    public CarbohydrateCalculatorPage selectMetricUnits() {
        waitAndClick(metricUnitsTab);
        return this;
    }

    public CarbohydrateCalculatorPage selectOtherUnits() {
        waitAndClick(otherUnitsTab);
        return this;
    }

    // -------------------------------------------------------------------------
    // Input helpers
    // -------------------------------------------------------------------------
    public CarbohydrateCalculatorPage enterAge(String age) {
        clearAndType(ageField, age);
        return this;
    }
    
    public CarbohydrateCalculatorPage selectMale() {
        scrollIntoView(maleRadio);
        if (!maleRadio.isSelected()) maleRadioButton.click();
        return this;
    }

    public CarbohydrateCalculatorPage selectFemale() {
        scrollIntoView(femaleRadio);
        if (!femaleRadio.isSelected()) femaleRadioButton.click();
        return this;
    }

    public CarbohydrateCalculatorPage enterHeightUS(String feet, String inches) {
        clearAndType(heightFeetField, feet);
        clearAndType(heightInchesField, inches);
        return this;
    }

    public CarbohydrateCalculatorPage enterWeightUS(String pounds) {
        clearAndType(weightPoundsField, pounds);
        return this;
    }

    public CarbohydrateCalculatorPage enterHeightMetric(String cm) {
        clearAndType(heightCmField, cm);
        return this;
    }

    public CarbohydrateCalculatorPage enterWeightMetric(String kg) {
        clearAndType(weightKgField, kg);
        return this;
    }

    /**
     * Select an activity level by its visible label text.
     * Accepted values (case-insensitive prefix match):
     *   "Sedentary", "Light", "Moderate", "Active", "Very Active", "Extra Active"
     */
    public CarbohydrateCalculatorPage selectActivityLevel(String activityLabel) {
        Select select = new Select(activityDropdown);
        select.getOptions().stream()
                .filter(opt -> opt.getText().toLowerCase()
                        .startsWith(activityLabel.toLowerCase()))
                .findFirst()
                .ifPresent(opt -> select.selectByVisibleText(opt.getText()));
        return this;
    }

    // -------------------------------------------------------------------------
    // Submit
    // -------------------------------------------------------------------------
    public CarbohydrateCalculatorPage clickCalculate() {
        scrollIntoView(calculateButton);
        waitAndClick(calculateButton);
        return this;
    }

    // -------------------------------------------------------------------------
    // Result / validation accessors
    // -------------------------------------------------------------------------
    public boolean isResultHeaderDisplayed() {
        try {
 
            return waitForVisibility(resultHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isResultSuggestionDisplayed() {
        try {
            return waitForVisibility(resultSuggestion).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isResultDisplayed() {
        try {
            return waitForVisibility(resultSection).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String theAgeValidationErrorDisplayedWithText(String msg) {
    	try {
    		return getResultText(ageFieldError);
    	} catch (Exception e) {
    		return "";
    	}
    }

    public String getResultText(WebElement w) {
        return waitForVisibility(w).getText().trim();
    }

    public String getActiveUnitTab() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                return driver.findElement(By.cssSelector("li#menuon"))
                        .getText().trim();
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                attempts++;
                if (attempts >= 3) {
                    throw e;
                }
            }
        }
        return ""; // Should not reach here
    }
    
    // -------------------------------------------------------------------------
    // Result table inspection
    // -------------------------------------------------------------------------
    /**
     * Returns the column headers from the first row of the result table.
     * Tries &lt;th&gt; elements first; falls back to &lt;td&gt; elements in the first &lt;tr&gt;
     * to handle tables that use &lt;td&gt; for header cells.
     */
    public List<String> getResultTableHeaders() {
        waitForVisibility(resultSection);
        List<WebElement> cells = resultSection.findElements(By.tagName("th"));
        if (cells.isEmpty()) {
            cells = resultSection.findElements(By.cssSelector("tr:first-child td"));
        }
        return cells.stream()
                .map(el -> el.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Returns all rows of the result table as a list of lists.
     * Each inner list represents one row; cells are read as &lt;th&gt; first,
     * falling back to &lt;td&gt; for rows without semantic header markup.
     */
    public List<List<String>> getResultTableData() {
        waitForVisibility(resultSection);
        return resultSection.findElements(By.tagName("tr")).stream()
                .map(row -> {
                    List<WebElement> cells = row.findElements(By.tagName("th"));
                    if (cells.isEmpty()) {
                        cells = row.findElements(By.tagName("td"));
                    }
                    return cells.stream()
                            .map(el -> el.getText().trim())
                            .filter(text -> !text.isEmpty())
                            .collect(Collectors.toList());
                })
                .filter(row -> !row.isEmpty())
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Age field validation helpers
    // -------------------------------------------------------------------------
    public CarbohydrateCalculatorPage clearAgeField() {
        waitForVisibility(ageField);
        ageField.click();
        // Select all text with Ctrl+A, then delete to trigger keyup event
        ageField.sendKeys(org.openqa.selenium.Keys.CONTROL, "a");
        ageField.sendKeys(org.openqa.selenium.Keys.DELETE);
        return this;
    }
    
    // Simulates losing focus on the age field by clicking on another element
    public CarbohydrateCalculatorPage blurAgeField() {
        scrollIntoView(ageField);
        ageField.click();
        // Click on the male radio button to move focus away from age field
        waitAndClick(titleHeader);
        return this;
    }
    
    public boolean isAgeErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cageifcErr")));
            return ageFieldError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAgeErrorMessage() {
        try {
            return getResultText(ageFieldError);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns the computed border color of the age input field.
     * Selenium reports colors in rgba() format — red = "rgba(255, 0, 0, 1)".
     * Falls back to border-top-color if the shorthand returns empty.
     */
    public String getAgeFieldBorderColor() {
        waitForVisibility(ageField);
        String color = ageField.getCssValue("border-color");
        if (color == null || color.isEmpty()) {
            color = ageField.getCssValue("border-top-color");
        }
        return color;
    }

    /**
     * Returns the computed background color of the age error message element (#cageifcErr).
     * Selenium reports colors in rgba() format — #ffcccc = "rgba(255, 204, 204, 1)".
     */
    public String getAgeErrorBackgroundColor() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cageifcErr")));
        return ageFieldError.getCssValue("background-color");
    }
    
    public boolean isErrorResultDisplayed() {
        try {
            return waitForVisibility(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
