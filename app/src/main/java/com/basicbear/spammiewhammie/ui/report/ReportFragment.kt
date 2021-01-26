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

class ReportFragment:Fragment() {

    companion object {


        val fragmentTag = "ReportFragment"
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }

    private lateinit var reportWebView: WebView
    private lateinit var phoneCall: PhoneCall
    private lateinit var personalInfo: PersonalInfo

    private lateinit var javascript_step1:String
    private lateinit var javascript_step2:String

    override fun onCreate(savedInstanceState: Bundle?) {

        personalInfo = PersonalInfo()
        personalInfo.getContactInfo(context!!)

        //personalInfo = arguments?.getParcelable(PersonalInfo_ExtraTag)!! //?: PersonalInfo()

        val numOnly:Regex = Regex("[^0-9]")
        personalInfo.MyPhoneNumber = personalInfo.MyPhoneNumber.replace(numOnly, "")

        javascript_step1 =
                "console.log(\"s Step1 \" + dnc_app.config.submit_complaint_url); " + //https://www2.donotcall.gov/save-complaint
                "window.ClearData=function(){return false;};" +
                "document.getElementById(\"PhoneTextBox\").value = '${personalInfo.MyPhoneNumber}'; " +
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
                "$(\"#FirstNameTextBox\").val(\"${personalInfo.FirstName}\"); "+
                "$(\"#LastNameTextBox\").val(${personalInfo.LastName}); "+
                "$(\"#StreetAddressTextBox\").val(${personalInfo.StreetAddress}); "+
                "$(\"#CityTextBox\").val(${personalInfo.City}); "+
                "$(\"#StateDropDownList\").val(${personalInfo.State}); "+
                "$(\"#ZipCodeTextBox\").val(${personalInfo.ZIP}); "+

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, "oncreateView triggered")
        val view = inflater.inflate(R.layout.report_fragment, container, false)

        reportWebView = view.findViewById(R.id.report_web_view)
        reportWebView.settings.javaScriptEnabled = true
        reportWebView.settings.javaScriptCanOpenWindowsAutomatically = true

        WebView.setWebContentsDebuggingEnabled(true)

        reportWebView.loadUrl(getString(R.string.federal_report_url) + getString(R.string.federal_report_url_step1_postfix))

        var loadingFinished = true
        var redirect = false

        reportWebView.webViewClient = object: WebViewClient(){

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                view?.loadUrl(request.url.toString())
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingFinished = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if(!redirect ){
                    loadingFinished = true
                    autoFillData(view,url)
                }else{
                    redirect=false
                }
            }
        }

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


        //"javascript:\$(document).ready(function() {" + javascript +"});"
        if(Build.VERSION.SDK_INT > 19){
            //view?.loadUrl("javascript:$(document).ready(function() {" + javascript +"});")
            //javascript = "document.addEventListener('DOMContentLoaded', function(event) {" + javascript + "})"
            //javascript = "window.onload= (function(){ " +javascript + "})();"
            view?.evaluateJavascript(javascript, ValueCallback {
                //s:String -> Log.d(TAG,"Value of EvaluateJavascript is working!")
            })
        }else{
            view?.loadUrl("javascript:window.onload= (function(){ " + javascript + "})();")
        }
    }


    fun updatePhoneCall(newPhoneCall: PhoneCall){
        phoneCall= newPhoneCall
    }

    fun WebViewCanGoBack():Boolean{
        return reportWebView.canGoBack()
    }

    fun WebViewGoBack(){
        reportWebView.goBack()
    }

    fun ClearWebView(){
        if (Build.VERSION.SDK_INT < 18) {
            reportWebView.clearView();
        } else {
            reportWebView.loadUrl("about:blank");
        }
    }
}