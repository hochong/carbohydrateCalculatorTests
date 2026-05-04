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
| Containerisation | Docker |

---

## Running the Tests

### Option 1 — Docker (recommended)

No Java, Maven, or Chrome installation required.

**Build the image:**
```bash
docker build -t carb-calc-tests .
```

**Run the tests:**
```bash
docker run --rm carb-calc-tests
```

**Extract the HTML report after the run:**
```bash
docker run --rm -v ${PWD}/target:/app/target carb-calc-tests
# Report: target/cucumber-reports/cucumber.html
```

Tests always run headless inside the container.

---

### Option 2 — Maven (local)

**Prerequisites:** Java 21+, Maven 3.6+, Google Chrome

**Visible browser window (default):**
```bash
mvn test
```

**Headless mode:**
```bash
mvn test -Dheadless=true
```

---

## Configuration

All settings live in [`src/test/resources/config.properties`](src/test/resources/config.properties):

| Key | Default | Description |
|---|---|---|
| `base.url` | calculator.net URL | Target application URL |
| `implicit.wait.seconds` | `10` | Selenium implicit wait |
| `explicit.wait.seconds` | `15` | WebDriverWait timeout |
| `page.load.timeout.seconds` | `30` | Page load timeout |
| `headless` | `false` | Run Chrome headlessly |

Any key can be overridden at runtime with a `-D` flag:
```bash
mvn test -Dheadless=true -Dexplicit.wait.seconds=20
```

---

## Project Structure

```
carbohydrateCalculatorTests/
├── Dockerfile                                   # Chrome + Maven image, runs tests headless
├── pom.xml                                      # Maven dependencies and build config
└── src/
    ├── main/java/
    │   ├── pages/
    │   │   ├── BasePage.java                    # Shared WebDriver helpers
    │   │   └── CarbohydrateCalculatorPage.java  # Page Object Model
    │   └── utils/
    │       ├── ConfigReader.java                # Loads config.properties
    │       └── DriverManager.java               # Thread-safe WebDriver lifecycle
    └── test/
        ├── java/
        │   ├── runner/TestRunner.java           # Cucumber JUnit entry point
        │   └── steps/CarbohydrateCalculatorSteps.java  # Step definitions
        └── resources/
            ├── config.properties               # Runtime configuration
            └── features/
                └── carbohydrate_calculator.feature  # BDD scenarios
```

---

## Test Reports

Generated under `target/cucumber-reports/` after each run:

| Format | Path |
|---|---|
| HTML | `target/cucumber-reports/cucumber.html` |
| JSON | `target/cucumber-reports/cucumber.json` |
| JUnit XML | `target/cucumber-reports/cucumber.xml` |

When running via Docker, mount the `target/` directory to access reports on the host:
```bash
docker run --rm -v ${PWD}/target:/app/target carb-calc-tests
```

---

## Scenarios

| Scenario | Type | Description |
|---|---|---|
| `USTab` | UI | Tab switching and US unit field visibility |
| `MaxAge` | Edge case | Calculation at boundary age (80) |
| `EmptyAgeFieldValidation` | Validation | Empty age field error handling |
| `StandardCalculationMetricModerate` | Happy path | Full metric calculation flow |
