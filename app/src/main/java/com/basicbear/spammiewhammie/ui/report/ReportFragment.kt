package com.basicbear.spammiewhammie.ui.report

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.basicbear.spammiewhammie.R
import com.basicbear.spammiewhammie.ui.contact_info.ContactInfoFragment
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.PhoneCall
import java.time.Instant

private const val TAG = "ReportFragment"
private const val contactInfoParameterTag = "ReportFragment ContactInfo Parameter"

class ReportFragment:Fragment(


) {

    interface Callbacks{
        fun closeSoftKeyboard(view:View)
    }

    companion object {
        val fragmentTag = "ReportFragment"
        fun newInstance(contactInfo:PersonalInfo): ReportFragment {
            return ReportFragment().apply {
                arguments= Bundle().apply {
                    putParcelable(contactInfoParameterTag, contactInfo)
                }
            }
        }
    }

    private val submit_complaint_url = "https://www2.donotcall.gov/save-complaint"

    private var callbacks:Callbacks? = null

    private lateinit var phoneCall: PhoneCall
    private lateinit var personalInfo: PersonalInfo
    private lateinit var submitModel:GovModel

    private lateinit var wasPrerecorded:Switch
    private lateinit var isMobileCall:Switch
    private lateinit var askedToStop:Switch
    private lateinit var haveDoneBusiness:Switch
    private lateinit var subjectMatter:Spinner
    private lateinit var callerName:EditText
    private lateinit var comments:EditText
    private lateinit var submitButton:Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        personalInfo = arguments?.getParcelable(contactInfoParameterTag)?: PersonalInfo()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.d(TAG, "oncreateView triggered")
        val view = inflater.inflate(R.layout.report_fragment, container, false)

        wasPrerecorded = view.findViewById(R.id.report_form_switch_wasPrerecorded)
        isMobileCall= view.findViewById(R.id.report_form_switch_isMobileCall)
        askedToStop= view.findViewById(R.id.report_form_switch_askedToStop)
        haveDoneBusiness= view.findViewById(R.id.report_form_switch_haveDoneBusiness)
        subjectMatter= view.findViewById(R.id.report_form_subjectMatter)
        callerName = view.findViewById(R.id.report_form_caller_name)

        comments= view.findViewById(R.id.report_form_comments)
        comments.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) callbacks?.closeSoftKeyboard(view)
        }

        submitButton = view.findViewById(R.id.report_submit_button)
        submitButton.setOnClickListener{
            Toast.makeText(context,"LEO is awesome", Toast.LENGTH_LONG).show()
        }

        return view
    }

    fun updatePhoneCall(newPhoneCall: PhoneCall){
        phoneCall= newPhoneCall
    }

    private fun loadFormData():GovModel{
        var newModel = GovModel(
                personalInfo.MyPhoneNumber,
                phoneCall.MediumDate(),
                phoneCall.DateHour().toString(),
                phoneCall.DateMinute().toString(),
                if(wasPrerecorded.isChecked) "Y" else "N",
                isMobileCall.isChecked.toString(),
                subjectMatter.selectedItem.toString(),
                phoneCall.number,
                callerName.text.toString(),
                if(haveDoneBusiness.isChecked) "Y" else "N" ,
                askedToStop.isChecked.toString(),
                personalInfo.FirstName,
                personalInfo.LastName,
                personalInfo.StreetAddress,
                personalInfo.StreetAddress2,
                personalInfo.City,
                personalInfo.State,
                personalInfo.ZIP,
                comments.text.toString(),
                "en",
                "Y"
            )
        return newModel
    }


}