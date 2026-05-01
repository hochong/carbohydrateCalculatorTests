package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for https://www.calculator.net/carbohydrate-calculator.html
 */
public class CarbohydrateCalculatorPage extends BasePage {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------
    public static final String PAGE_URL =
            "https://www.calculator.net/carbohydrate-calculator.html";
    
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
    /** The main result section that appears after a successful calculation. */
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
    /** Returns true when the result section is visible on the page. */
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
        return driver.findElement(
                org.openqa.selenium.By.cssSelector("li#menuon"))
                .getText().trim();
    }
}
