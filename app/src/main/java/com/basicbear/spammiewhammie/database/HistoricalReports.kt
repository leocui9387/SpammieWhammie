package com.basicbear.spammiewhammie.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Report::class], version =1)
abstract class HistoricalReports:RoomDatabase() {
    abstract fun reportDAO():ReportDAO
}