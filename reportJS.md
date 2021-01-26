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

# 
