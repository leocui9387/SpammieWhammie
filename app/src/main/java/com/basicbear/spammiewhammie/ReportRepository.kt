package com.basicbear.spammiewhammie

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.basicbear.spammiewhammie.database.HistoricalReports
import com.basicbear.spammiewhammie.database.Report
import com.basicbear.spammiewhammie.database.ReportDAO
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private val DATABASE_NAME = "report_database"

class ReportRepository private constructor(context: Context){
    companion object{

        private var instance:ReportRepository? = null

        fun initialize(context: Context){
            if (instance == null){
                instance = ReportRepository(context)
            }
        }

        fun get():ReportRepository{
            return instance?:
                throw IllegalStateException("ReportRepository must be initialized")
        }
    }

    private val database:HistoricalReports = Room.databaseBuilder(
        context.applicationContext,
        HistoricalReports::class.java,
        DATABASE_NAME
    ).build()

    private val reportDAO:ReportDAO = database.reportDAO()
    private val executor= Executors.newSingleThreadExecutor()

    fun getReports():LiveData<List<Report>> = reportDAO.getReports()
    fun getReport(uuid:UUID):Report? = reportDAO.getReport(uuid)

    fun update(report:Report){
        executor.execute {
            reportDAO.update(report)
        }
    }

    fun add(report: Report){
        executor.execute {
            reportDAO.add(report)
        }
    }
    fun delete(report: Report){
        executor.execute {
            reportDAO.delete(report)
        }
    }

}