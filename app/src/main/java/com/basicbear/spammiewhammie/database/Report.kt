package com.basicbear.spammiewhammie.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basicbear.spammiewhammie.ui.gov.GovModel
import java.util.*

@Entity(tableName = "historical_reports")
data class Report(
    @PrimaryKey val id:UUID = UUID.randomUUID(),
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
):Parcelable {

    constructor(parcel: Parcel) : this(
        UUID.fromString(parcel.readString()?:"parsel error"),
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error",
        parcel.readString()?:"parsel error"
    ) {
    }

    fun equals(report:Report):Boolean{
        return this.phoneNumber.trim().equals(report.phoneNumber.trim(),true)  &&
               this.dateOfCall.trim().equals(report.dateOfCall.trim(),true)  &&
               this.timeOfCall.trim().equals(report.timeOfCall.trim(),true)  &&
               this.minuteOfCall.trim().equals(report.minuteOfCall.trim(),true)  &&
               this.companyPhoneNumber.trim().equals(report.companyPhoneNumber.trim(),true)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phoneNumber)
        parcel.writeString(dateOfCall)
        parcel.writeString(timeOfCall)
        parcel.writeString(minuteOfCall)
        parcel.writeString(wasPrerecorded)
        parcel.writeString(isMobileCall)
        parcel.writeString(subjectMatter)
        parcel.writeString(companyPhoneNumber)
        parcel.writeString(companyName)
        parcel.writeString(haveDoneBusiness)
        parcel.writeString(askedToStop)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(streetAddress1)
        parcel.writeString(streetAddress2)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(zip)
        parcel.writeString(comments)
        parcel.writeString(language)
        parcel.writeString(validFlag)
        parcel.writeString(reportStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Report> {
        override fun createFromParcel(parcel: Parcel): Report {
            return Report(parcel)
        }

        override fun newArray(size: Int): Array<Report?> {
            return arrayOfNulls(size)
        }
    }

}