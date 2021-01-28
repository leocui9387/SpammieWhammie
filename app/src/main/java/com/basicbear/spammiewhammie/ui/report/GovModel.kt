package com.basicbear.spammiewhammie.ui.report

import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.PhoneCall

data class GovModel(

    var phoneNumber:String,
    var dateOfCall:String,
    var timeOfCall:String,
    var minuteOfCall:String,
    var wasPrerecorded:String,
    var isMobileCall:String,
    var subjectMatter:String,
    var companyPhoneNumber:String,
    var companyName:String,
    var haveDoneBusiness:String,
    var askedToStop:String,
    var firstName:String,
    var lastName:String,
    var streetAddress1:String,
    var streetAddress2:String,
    var city:String,
    var state:String,
    var zip:String,
    var comments:String,
    var language:String,
    var validFlag:String,

) {
    fun loadPersonalInfo(contactInfo: PersonalInfo){
        firstName =contactInfo.FirstName
        lastName=contactInfo.LastName
        phoneNumber=contactInfo.MyPhoneNumber
        streetAddress1=contactInfo.StreetAddress
        streetAddress2=contactInfo.StreetAddress2
        state=contactInfo.State
        city=contactInfo.City
        zip = contactInfo.ZIP
    }
    fun loadPhoneCall(phoneCall: PhoneCall){

        companyPhoneNumber=phoneCall.number
        dateOfCall=phoneCall.MediumDate()
        timeOfCall= phoneCall.DateHour().toString()
        minuteOfCall= phoneCall.DateMinute().toString()

    }


}