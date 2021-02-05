package com.basicbear.spammiewhammie.ui.report

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.basicbear.spammiewhammie.R
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.PhoneCall

private const val TAG = "ReportFragment"

class ReportFragment(
        private val contactInfo:PersonalInfo,
        private val phoneCall: PhoneCall,
        ):Fragment() {

    interface Callbacks{
        fun onComplaintSubmission()
    }


    private lateinit var reportWebView: WebView

    private lateinit var javascript_step1:String
    private lateinit var javascript_step2:String

    override fun onCreate(savedInstanceState: Bundle?) {


        javascript_step1 =
                "console.log(\"s Step1 \" + dnc_app.config.submit_complaint_url); " + //https://www2.donotcall.gov/save-complaint
                "window.ClearData=function(){return false;};" +
                "document.getElementById(\"PhoneTextBox\").value = '${contactInfo.MyPhoneNumber}'; " +
                "$(\"#DateOfCallTextBox\").val(\"${phoneCall.MediumDate()}\"); "+
                "$(\"#TimeOfCallDropDownList\").val(${phoneCall.DateHour()}); "+
                "$(\"#ddlMinutes\").val(${phoneCall.DateMinute()}); " +
                "$(\"#PhoneCallRadioButton\").prop('checked',true); ";

                /*
                "$(\"#PhoneTextBox\").val(\"${personalInfo.MyPhoneNumber}\"); " +
                "$(\"#DateOfCallTextBox\").val(\"${phoneCall.MediumDate()}\"); "+
                "$(\"#TimeOfCallDropDownList\").val(${phoneCall.DateHour()}); "+
                "$(\"#ddlMinutes\").val(${phoneCall.DateMinute()}); " +
                "$(\"#PhoneCallRadioButton\").prop('checked',true); "
                 */

        javascript_step2 =" console.log(\"js Step2\"); " +
                "$(\"#CallerPhoneNumberTextBox\").val(\"${phoneCall.number}\"); " +
                "$(\"#FirstNameTextBox\").val(\"${contactInfo.FirstName}\"); "+
                "$(\"#LastNameTextBox\").val(${contactInfo.LastName}); "+
                "$(\"#StreetAddressTextBox\").val(${contactInfo.StreetAddress}); "+
                "$(\"#CityTextBox\").val(${contactInfo.City}); "+
                "$(\"#StateDropDownList\").val(${contactInfo.State}); "+
                "$(\"#ZipCodeTextBox\").val(${contactInfo.ZIP}); "+

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, "oncreateView triggered")
        val view = inflater.inflate(R.layout.report_fragment, container, false)

        reportWebView = view.findViewById(R.id.report_web_view)
        reportWebView.settings.javaScriptEnabled = true
        reportWebView.webViewClient = WebViewClient()
        WebView.setWebContentsDebuggingEnabled(true)

        reportWebView.loadUrl(getString(R.string.federal_report_url) + getString(R.string.federal_report_url_step1_postfix))



        return view
    }

    fun autoFillData(view: WebView?, url: String?){
        var javascript =""

        if(url.equals(getString(R.string.federal_report_url) + getString(R.string.federal_report_url_step1_postfix))){
            javascript = javascript_step1
        }else if(url.equals(getString(R.string.federal_report_url) + getString(R.string.federal_report_url_step2_postfix))){
            javascript = javascript_step2
        }else if(url.equals(getString(R.string.federal_report_url) + getString(R.string.federal_report_url_step3_postfix))){
            //return to main activity
        }

    }

    fun WebViewCanGoBack():Boolean{
        return reportWebView.canGoBack()
    }

    fun WebViewGoBack(){
        reportWebView.goBack()
    }

}