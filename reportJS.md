# Important Constants
- dnc_app.config.submit_complaint_url : https://www2.donotcall.gov/save-complaint


# Upload Model
- phoneNumber
- dateOfCall
- timeOfCall
- minuteOfCall
- wasPrerecorded
- isMobileCall
- subjectMatter
- companyPhoneNumber
- companyName
- haveDoneBusiness
- askedToStop
- firstName
- lastName
- streetAddress1
- streetAddress2
- city
- state
- zip
- comments
- language
- validFlag

# Program Flow
1. Step 1
  - Validation
    - Send to URL dnc_app.config.complaint_step2_url
      - Only data _model.phoneNumber, _model.dateOfCall
  - Outcomes
    - DONE Outcomes
      - 0 (full success)
      - partials/report/step2InvalidPhoneRegistration
      - partials/report/step2InGracePeriod
      - Issue: run dnc_app.utils.getSupportId(e) 
    - FAIL Outcomes [1]
2. Step 2
  - Final validation
    - validFlag not ""
    - run chkConsumerCompanyPhone()
  - 4 possible outcomes

# Other Notes
- Subject Matter # 8 -> "partials/report/step2DebtCollectorCall"
- Subject Matter #15 -> "partials/report/step2SurveyCall"
- Text Message Step 2 -> "partials/report/step2MobileCall"
- Subject Matter Values
  - 0 Unknown
  - 1 Other
  - 2 Dropped call or no message
  - 3 Reducing your debt (credit cards, mortgage, student loans)
  - 4 Calls pretending to be government, businesses, or family and friends
  - 5 Medical  &amp; prescriptions
  - 6 Home security  &amp; alarms
  - 7 Computer  &amp; technical support
  - 8 Debt collection
  - 9 Energy, solar,  &amp; utilities
  - 10 Home improvement  &amp; cleaning
  - 11 Work from home  &amp; other ways to make money
  - 12 Warranties  &amp; protection plans
  - 13 Lotteries, prizes  &amp; sweepstakes
  - 14 Vacation  &amp; timeshares
  - 15 Surveys  &amp; political calls
  - 16 Charities
