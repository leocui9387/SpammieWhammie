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
import android.widget.Button
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
    private lateinit var loadInfoButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, "oncreateView triggered")
        val view = inflater.inflate(R.layout.report_fragment, container, false)

        loadInfoButton = view.findViewById(R.id.report_auto_load_button)
        loadInfoButton.setOnClickListener {

            val js = scriptSelector (reportWebView.url?:"")
            Log.d(TAG,"javascript run: " + js)
            reportWebView.evaluateJavascript(js,null)

        }

        reportWebView = view.findViewById(R.id.report_web_view)
        reportWebView.settings.javaScriptEnabled = true
        reportWebView.webViewClient = WebViewClient()

        reportWebView.loadUrl(getString(R.string.federal_report_url) + getString(R.string.federal_report_url_step1_postfix))

        return view
    }

    private fun scriptSelector(url: String): String{
        return if(url.contains("step1",true)){
                    "document.getElementById(\"PhoneTextBox\").value = '${contactInfo.MyPhoneNumber}'; " +
                            "$(\"#DateOfCallTextBox\").val(\"${phoneCall.MediumDate()}\"); "+
                            "$(\"#TimeOfCallDropDownList\").val(${phoneCall.DateHour()}); "+
                            "$(\"#ddlMinutes\").val(${phoneCall.DateMinute()}); " +
                            "$(\"#PhoneCallRadioButton\").prop('checked',true); ";
                }
                else{

                    "$(\"#CallerPhoneNumberTextBox\").val(\"${phoneCall.number}\"); " +
                            "$(\"#FirstNameTextBox\").val(\"${contactInfo.FirstName}\"); "+
                            "$(\"#LastNameTextBox\").val(\"${contactInfo.LastName}\"); "+
                            "$(\"#StreetAddressTextBox\").val(\"${contactInfo.StreetAddress}\"); "+
                            "$(\"#CityTextBox\").val(\"${contactInfo.City}\"); "+
                            "$(\"#StateDropDownList\").val(\"${contactInfo.State}\"); "+
                            "$(\"#ZipCodeTextBox\").val(\"${contactInfo.ZIP}\"); "
                }
    }

    fun WebViewCanGoBack():Boolean{
        return reportWebView.canGoBack()
    }

    fun WebViewGoBack(){
        reportWebView.goBack()
    }

}