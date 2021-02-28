package com.basicbear.spammiewhammie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Report::class], version =1)
@TypeConverters(ReportTypeConverters::class)
abstract class HistoricalReports:RoomDatabase() {

    abstract fun reportDAO():ReportDAO

}