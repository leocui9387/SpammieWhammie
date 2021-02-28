package com.basicbear.spammiewhammie.ui.history

import androidx.lifecycle.ViewModel
import com.basicbear.spammiewhammie.ReportRepository

class ReportHistoryViewModel: ViewModel() {

    private val reportRepo = ReportRepository.get()
    val reportListLiveData = reportRepo.getReports()


}