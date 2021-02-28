package com.basicbear.spammiewhammie.ui.gov

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basicbear.spammiewhammie.NavigationCallbacks
import com.basicbear.spammiewhammie.R
import com.basicbear.spammiewhammie.database.Report
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo

private const val TAG = "ReportFragment"
private const val PARAM_contactInfo = TAG + "_ContactInfo"
private const val PARAM_uri = TAG + "_TaskUrl"
private const val PARAM_showOpenWindow = TAG + "_ShowOpenWindow"

class ReportFragment():Fragment() {

    companion object{

        fun newInstance(
                contactInfo: PersonalInfo,
                task_url: String,
                show_open_window_button:Boolean,
                report: Report?
        ): ReportFragment {
            return ReportFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_contactInfo,contactInfo)
                    putString(PARAM_uri,task_url)
                    putBoolean(PARAM_showOpenWindow,show_open_window_button )
                }
            }
        }
    }
    private lateinit var contactInfo: PersonalInfo
    private lateinit var task_url: Uri
    private var show_open_window_button:Boolean? = null

    private lateinit var reportWebView: WebView
    private lateinit var loadInfoButton: Button
    private lateinit var doneButton: Button
    private lateinit var openCallWindowButton: Button

    private lateinit var govModelJsInterface:GovModelJsInterface

    private var callbacks: NavigationCallbacks? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavigationCallbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactInfo = arguments?.getParcelable(PARAM_contactInfo)?:PersonalInfo()
        task_url = Uri.parse(arguments?.getString(PARAM_uri)?:"")
        show_open_window_button = arguments?.getBoolean(PARAM_showOpenWindow)

        requireActivity().actionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.report_fragment, container, false)
        govModelJsInterface = GovModelJsInterface()

        openCallWindowButton = view.findViewById(R.id.report_call_window_button)
        openCallWindowButton.setOnClickListener {
            openCallWindow()
        }
        if (!(show_open_window_button?:false)){openCallWindowButton.isEnabled = false}

        doneButton = view.findViewById(R.id.report_done_button)
        doneButton.setOnClickListener {

            callbacks?.goto_main()
        }

        loadInfoButton = view.findViewById(R.id.report_auto_load_button)
        loadInfoButton.setOnClickListener {
            val js = scriptSelector(reportWebView.url ?: "")
            reportWebView.evaluateJavascript(js, null)
        }

        reportWebView = view.findViewById(R.id.report_web_view)
        reportWebView.settings.javaScriptEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        reportWebView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String
            ): Boolean {

                if(url.contains(task_url.path?:"")){
                    return false
                }
                else{
                    Toast.makeText(requireContext(),"Please do not browse the web with this app",Toast.LENGTH_LONG).show()
                    return true
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (isReportPage(task_url)) {
                    reportWebView.addJavascriptInterface(govModelJsInterface,"spammiewhammie")
                }
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (isReportPage(task_url)){
                    val js = getString(R.string.js_gov_model_saver)
                    reportWebView.evaluateJavascript("javascript:" + js,null)
                }

            }

            private fun isReportPage(uri: Uri):Boolean{
                return uri.path?.contains(getString(R.string.federal_report_url_postfix), true)?:false
            }

        }

        reportWebView.loadUrl(task_url.toString())

        return view
    }

    private fun scriptSelector(url: String): String{
        return when{
            url.contains(getString(R.string.federal_report_url_postfix), true) ->{
                if(url.contains("step1", true)){
                    "$(\"#PhoneTextBox\").val(\"${contactInfo.MyPhoneNumber}\"); "
                }else{
                    "$(\"#FirstNameTextBox\").val(\"${contactInfo.FirstName}\"); "+
                    "$(\"#LastNameTextBox\").val(\"${contactInfo.LastName}\"); "+
                    "$(\"#StreetAddressTextBox\").val(\"${contactInfo.StreetAddress}\"); "+
                    "$(\"#CityTextBox\").val(\"${contactInfo.City}\"); "+
                    "$(\"#StateDropDownList\").val(\"${contactInfo.State}\"); "+
                    "$(\"#ZipCodeTextBox\").val(\"${contactInfo.ZIP}\"); "
                }
            }
            url.contains(getString(R.string.federal_verify_registration_url_postfix), true) ->{
                "$(\"#VerifyPhoneNumberTextBox1\").val(\"${contactInfo.MyPhoneNumber}\"); " +
                "$(\"#VerifyEmailAddressTextBox\").val(\"${contactInfo.MyEmail}\"); "
            }

            url.contains(getString(R.string.federal_registration_url_postfix), true) ->{
                "$(\"#ContentPlaceHolder1_PhoneNumberTextBox1\").val(\"${contactInfo.MyPhoneNumber}\"); " +
                "$(\"#ContentPlaceHolder1_EmailAddressTextBox\").val(\"${contactInfo.MyEmail}\"); " +
                "$(\"#ContentPlaceHolder1_ConfirmEmailAddressTextBox\").val(\"${contactInfo.MyEmail}\"); "
            }

            else -> {
                "BasicBear"
            }
        }
    }

    fun WebViewCanGoBack():Boolean{
        return reportWebView.canGoBack()
    }

    fun WebViewGoBack(){
        reportWebView.goBack()
    }

    private fun openCallWindow(){

            // open call app
            val intent =  Intent(Intent.ACTION_CALL_BUTTON)

            startActivity(intent)

    }
}