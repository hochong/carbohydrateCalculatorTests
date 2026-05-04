Feature: Carbohydrate Calculator

  Background:
    Given the user opens the Carbohydrate Calculator page
   
  # --------------------------------------------------------------------------
  # Scenario 2 — UIUX Testing: Switching to US Units tab
  # --------------------------------------------------------------------------
  Scenario: USTab
    Given the user selects "metric units"
    Given the user selects "US units"
    
    Then  "US Units" tab is selected
  	
  	# Use inputs steps to check feet/inches/pounds input text box displayed
	  When  the user enters height of "6" feet and "5" inches   
    And  the user enters weight of "155" pounds
    
  # --------------------------------------------------------------------------
  # Scenario 7 — Edge Case: Max age
  # --------------------------------------------------------------------------
  Scenario: MaxAge
    Given the user selects "metric units"
    
    When  the user enters age "80"
    And   the user clicks the Calculate button
    
    Then  the result header should be displayed
    And   the result suggestion should be displayed
    And   the result section should be displayed

  # --------------------------------------------------------------------------
  # Scenario 13 — Happy path: Standard calculation metric units moderate female
  # --------------------------------------------------------------------------
  Scenario: StandardCalculationMetricModerate
    Given the user selects "metric units"
    
    When  the user enters age "35"
    And   the user selects gender "female"
    And   the user enters height of "140" cm
    And   the user enters weight of "60" kg
    And   the user selects activity level "Moderate"
    And   the user clicks the Calculate button
    
    Then  the result header should be displayed
    And   the result suggestion should be displayed
    And   the result section should be displayed

  # --------------------------------------------------------------------------
  # Scenario 13Alt — Happy path + result table column header verification
  # --------------------------------------------------------------------------
  Scenario: StandardCalculationMetricModerate13Alt
    Given the user selects "metric units"

    When  the user enters age "35"
    And   the user selects gender "female"
    And   the user enters height of "140" cm
    And   the user enters weight of "60" kg
    And   the user selects activity level "Moderate"
    And   the user clicks the Calculate button

    Then  the result header should be displayed
    And   the result suggestion should be displayed
    And   the result section should be displayed:
      | Goal | Daily Calorie Allowance | 40%* | 55%* | 65%* | 75%* |

  # --------------------------------------------------------------------------
  # Scenario 19 — Validation: Empty age field
  # --------------------------------------------------------------------------
  Scenario: EmptyAgeFieldValidation
    Given the user selects "metric units"
    
    When  the user clears the age field
    And   the user loses focus on the age field
    
    Then  the age validation error message should be displayed
    
    When  the user clicks the Calculate button
    
    Then  an error result should be shown