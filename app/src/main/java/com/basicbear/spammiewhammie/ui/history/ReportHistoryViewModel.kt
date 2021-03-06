package com.basicbear.spammiewhammie.ui.history

import androidx.lifecycle.ViewModel
import com.basicbear.spammiewhammie.ReportRepository
import com.basicbear.spammiewhammie.database.Report
import java.util.*

class ReportHistoryViewModel: ViewModel() {

    private val reportRepo = ReportRepository.get()
    val reportListLiveData = reportRepo.getReports()

    fun delete(report: Report){
        reportRepo.delete(report)
    }

}