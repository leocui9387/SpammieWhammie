package com.basicbear.spammiewhammie.ui.gov

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.lifecycle.LifecycleOwner
import com.basicbear.spammiewhammie.ReportRepository
import com.basicbear.spammiewhammie.database.Report
import com.google.gson.Gson

import androidx.lifecycle.Observer

private val TAG = "GovModelJsInterface"

class GovModelJsInterface(private val lifecycleOwner: LifecycleOwner, private val reportRepo: ReportRepository) {

    lateinit var submitData: GovModel
    lateinit var result: String
    private var reportSaved:Boolean = false

    @JavascriptInterface
    fun passModel(model:String, responseBody:String){
        if(reportSaved) return

        val gson = Gson()
        val modelBuff = gson.fromJson(model, GovModel::class.java)

        submitData =    if(modelBuff !=null) modelBuff
                        else GovModel("")

        result = responseBody

        add2Database(modelBuff)

        reportSaved = true

    }

    private fun add2Database(model:GovModel){
        val newReport = Report()
        updateReport(newReport,model)
        reportRepo.add(newReport)
        /*
        val buffReport = reportRepo.getReport(model.phoneNumber,model.companyPhoneNumber,model.dateOfCall,model.timeOfCall,model.minuteOfCall)
        buffReport.observe( lifecycleOwner ,
            Observer { report -> report?.let {
                Log.d(TAG,"report: " + report.companyName)

                    if (report != null){
                        updateReport(report,model)
                        reportRepo.update(report)
                    }else{
                        val newReport = Report()
                        updateReport(newReport,model)

                    }
                }
            }
        )

         */
    }

    private fun updateReport(report:Report,model:GovModel){

        report.phoneNumber = model.phoneNumber
        report.dateOfCall = model.dateOfCall
        report.timeOfCall = model.timeOfCall
        report.minuteOfCall = model.minuteOfCall
        report.wasPrerecorded = model.wasPrerecorded
        report.isMobileCall = model.isMobileCall
        report.subjectMatter = model.subjectMatter
        report.companyPhoneNumber = model.companyPhoneNumber
        report.companyName = model.companyName
        report.haveDoneBusiness = model.haveDoneBusiness
        report.askedToStop = model.askedToStop
        report.firstName = model.firstName
        report.lastName = model.lastName
        report.streetAddress1 = model.streetAddress1
        report.streetAddress2 = model.streetAddress2
        report.city = model.city
        report.state = model.state
        report.zip = model.zip
        report.comments = model.comments
        report.language = model.language
        report.validFlag = model.validFlag
        report.reportStatus = "updated"
    }
}