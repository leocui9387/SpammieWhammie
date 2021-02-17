package com.basicbear.spammiewhammie.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import java.util.*

@Dao
interface ReportDAO {
    @Query("SELECT * FROM historical_reports")
    fun getReports(): LiveData<List<Report>>

    @Query("SELECT * FROM historical_reports WHERE phoneNumber = (:receipient_number) AND companyPhoneNumber =(:caller_number) AND dateOfCall = (:date) AND timeOfCall = (:time) AND minuteOfCall=(:minute)")
    fun getReport(receipient_number:String,caller_number:String, date: String,time:String,minute:String):LiveData<Report?>
}