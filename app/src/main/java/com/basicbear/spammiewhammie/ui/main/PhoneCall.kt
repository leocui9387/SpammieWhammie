package com.basicbear.spammiewhammie.ui.main

import android.os.Parcel
import android.os.Parcelable
import android.provider.CallLog
import android.telephony.PhoneNumberUtils
import java.text.SimpleDateFormat
import java.util.*

data class PhoneCall(
    var number:String = "",
    var via_number:String = "",
    var type: Int = -1,
    var date: Long = -1,
    var duration: Long = -1
) {


    override fun toString(): String {
        return "Number: ${number}|Type: ${type}|Date: ${Date(date)}|Duration: ${duration}s"
    }
    fun MediumDate():String{

        val formatter = SimpleDateFormat("MM/dd/yyyy")
        val parsedDate: Date = Date(date)

        return formatter.format(Date(date))
    }
    fun DateHour():Int = Date(date).hours
    fun DateMinute():Int = Date(date).minutes

    fun FormatedNumber():String{
        return PhoneNumberUtils.formatNumber(number,"US")
    }
    fun TypeString():String{

            when (type) {
                CallLog.Calls.OUTGOING_TYPE -> return "OUTGOING"
                CallLog.Calls.INCOMING_TYPE -> return "INCOMING"
                CallLog.Calls.MISSED_TYPE -> return "MISSED"
                CallLog.Calls.VOICEMAIL_TYPE -> return "VOICEMAIL"
                CallLog.Calls.REJECTED_TYPE -> return "REJECTED"
                CallLog.Calls.BLOCKED_TYPE  -> return "BLOCKED"
                CallLog.Calls.ANSWERED_EXTERNALLY_TYPE  -> return "ANSWERED_EXTERNALLY"
            }
        return "ERROR"
    }

}