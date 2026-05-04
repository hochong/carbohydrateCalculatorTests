# Carbohydrate Calculator E2E Tests

Selenium + Cucumber BDD test suite for [calculator.net/carbohydrate-calculator](https://www.calculator.net/carbohydrate-calculator.html).

## Tech Stack

| Layer | Tool |
|---|---|
| Language | Java 21 |
| Test framework | Cucumber 7 + JUnit 4 |
| Browser automation | Selenium 4 |
| Driver management | WebDriverManager |
| Assertions | AssertJ |
| Build | Maven |

## Prerequisites

- Java 21+
- Maven 3.6+
- Google Chrome installed

## Running the Tests

**Default (visible browser window):**
```bash
mvn test
```

**Headless mode (for CI or no display):**
```bash
mvn test -Dheadless=true
```

## Configuration

All settings live in [`src/test/resources/config.properties`](src/test/resources/config.properties):

| Key | Default | Description |
|---|---|---|
| `base.url` | calculator.net URL | Target application URL |
| `implicit.wait.seconds` | `10` | Selenium implicit wait |
| `explicit.wait.seconds` | `15` | WebDriverWait timeout |
| `page.load.timeout.seconds` | `30` | Page load timeout |
| `headless` | `false` | Run Chrome headlessly |

Any key can be overridden at runtime via a `-D` system property:
```bash
mvn test -Dheadless=true -Dexplicit.wait.seconds=20
```

## Project Structure

```
src/
├── main/java/
│   ├── pages/
│   │   ├── BasePage.java                 # Shared WebDriver helpers
│   │   └── CarbohydrateCalculatorPage.java  # Page Object Model
│   └── utils/
│       ├── ConfigReader.java             # Loads config.properties
│       └── DriverManager.java            # Thread-safe WebDriver lifecycle
└── test/
    ├── java/
    │   ├── runner/TestRunner.java         # Cucumber JUnit entry point
    │   └── steps/CarbohydrateCalculatorSteps.java  # Step definitions
    └── resources/
        ├── config.properties             # Runtime configuration
        └── features/
            └── carbohydrate_calculator.feature  # BDD scenarios
```

## Test Reports

Reports are generated under `target/cucumber-reports/` after each run:

| Format | Path |
|---|---|
| HTML | `target/cucumber-reports/cucumber.html` |
| JSON | `target/cucumber-reports/cucumber.json` |
| JUnit XML | `target/cucumber-reports/cucumber.xml` |

## Scenarios

| Scenario | Type | Description |
|---|---|---|
| `USTab` | UI | Tab switching and field visibility |
| `MaxAge` | Edge case | Calculation at boundary age (80) |
| `EmptyAgeFieldValidation` | Validation | Empty age field error handling |
| `StandardCalculationMetricModerate` | Happy path | Full metric calculation flow |
