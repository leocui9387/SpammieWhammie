package com.basicbear.spammiewhammie.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "historical_reports")
data class Report(
    @PrimaryKey val id:UUID=UUID.randomUUID(),
    var phoneNumber:String ="",
    var dateOfCall:String ="",
    var timeOfCall:String ="",
    var minuteOfCall:String ="",
    var wasPrerecorded:String ="",
    var isMobileCall:String ="",
    var subjectMatter:String ="",
    var companyPhoneNumber:String ="",
    var companyName:String ="",
    var haveDoneBusiness:String ="",
    var askedToStop:String ="",
    var firstName:String ="",
    var lastName:String ="",
    var streetAddress1:String ="",
    var streetAddress2:String ="",
    var city:String ="",
    var state:String ="",
    var zip:String ="",
    var comments:String ="",
    var language:String ="",
    var validFlag:String ="",
    var reportStatus:String =""
) {
}