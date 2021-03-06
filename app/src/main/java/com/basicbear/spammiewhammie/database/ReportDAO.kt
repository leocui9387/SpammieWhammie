package com.basicbear.spammiewhammie.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface ReportDAO {
    @Query("SELECT * FROM historical_reports")
    fun getReports(): LiveData<List<Report>>

//    @Query("SELECT * FROM historical_reports WHERE id = :uuid")
//    fun getReport(uuid: UUID):Report?

    @Update
    fun update(report:Report)

    @Insert
    fun add(report:Report)


    //@Query("DELETE FROM historical_reports WHERE id=:uuid")
    //fun delete(uuid: UUID)

    @Delete
    fun delete(report: Report)
}