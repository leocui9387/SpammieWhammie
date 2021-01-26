# Important Constants
- dnc_app.config.submit_complaint_url : https://www2.donotcall.gov/save-complaint


# Upload Model
phoneNumber: "",
dateOfCall: "",
timeOfCall: "",
minuteOfCall: "",
wasPrerecorded: "",
isMobileCall: "",
subjectMatter: "",
companyPhoneNumber: "",
companyName: "",
haveDoneBusiness: "",
askedToStop: "",
firstName: "",
lastName: "",
streetAddress1: "",
streetAddress2: "",
city: "",
state: "",
zip: "",
comments: "",
language: "",
validFlag: ""

# Program Flow
1. Step 1
  - Validation 
  - Outcomes
    - DONE Outcomes [3]
      - 0 (full success)
      - partials/report/step2InvalidPhoneRegistration
      - partials/report/step2InGracePeriod
    - FAIL Outcomes [1]
2. Step 2
  - Final validation 
  - 4 possible outcomes

# 
