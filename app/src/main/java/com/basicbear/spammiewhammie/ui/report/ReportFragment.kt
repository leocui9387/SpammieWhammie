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
import com.basicbear.spammiewhammie.Validation.AreaCodes
import com.basicbear.spammiewhammie.Validation.GovModel
import com.basicbear.spammiewhammie.Validation.DoNotCallApi
import com.basicbear.spammiewhammie.Validation.Validation
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.PhoneCall

private const val TAG = "ReportFragment"
private const val contactInfoParameterTag = "ReportFragment ContactInfo Parameter"

class ReportFragment(
        private val contactInfo:PersonalInfo,
        private val phoneCall: PhoneCall,
        private val areaCodes: AreaCodes,
        private val dncApi: DoNotCallApi
        ):Fragment() {

    interface Callbacks{
        fun closeSoftKeyboard(view:View)
        fun onComplaintSubmission()
    }

    private var callbacks:Callbacks? = null
    private lateinit var validation: Validation

    private lateinit var submitModel: GovModel

    private lateinit var wasPrerecorded:RadioGroup
    private lateinit var isMobileCall:RadioGroup
    private lateinit var askedToStop:RadioGroup
    private lateinit var haveDoneBusiness:RadioGroup
    private lateinit var subjectMatter:Spinner
    private lateinit var callerName:EditText
    private lateinit var comments:EditText
    private lateinit var submitButton:Button

    private var viewCreated:Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.report_fragment, container, false)

        wasPrerecorded = view.findViewById(R.id.report_form_wasPrerecorded_radio)
        isMobileCall= view.findViewById(R.id.report_form_isMobileCall_radio)
        askedToStop= view.findViewById(R.id.report_form_askedToStop_radio )
        haveDoneBusiness= view.findViewById(R.id.report_form_haveDoneBusiness_radio)

        subjectMatter= view.findViewById(R.id.report_form_subjectMatter)
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.report_form_subject_matter_spinner,
                android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            subjectMatter.adapter=arrayAdapter
        }

        callerName = view.findViewById(R.id.report_form_caller_name)
        callerName.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) callbacks?.closeSoftKeyboard(view)
        }

        comments= view.findViewById(R.id.report_form_comments)
        comments.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) callbacks?.closeSoftKeyboard(view)
        }

        submitButton = view.findViewById(R.id.report_submit_button)
        submitButton.setOnClickListener{

            submitModel = loadFormData()
            validation = Validation(requireContext(),submitModel,dncApi,areaCodes)
            Log.d(TAG,"validation text: "+ validation.validate())
            Toast.makeText(context,"Validation Text: " , Toast.LENGTH_LONG).show()
            callbacks?.onComplaintSubmission()
        }
        viewCreated = true
        return view
    }


    private fun loadFormData(): GovModel {
        var newModel = GovModel(
                PersonalInfo.numbersOnly(contactInfo.MyPhoneNumber),
                phoneCall.MediumDate(),
                phoneCall.DateHour().toString(),
                phoneCall.DateMinute().toString(),
                wasPrerecorded_GovModel(),
                isMobileCall_GovModel(),
                subjectMatter.selectedItem.toString(),
                PersonalInfo.numbersOnly(phoneCall.number),
                callerName.text.toString(),
                haveDoneBusiness_GovModel(),
                askedToStop_GovModel(),
                contactInfo.FirstName,
                contactInfo.LastName,
                contactInfo.StreetAddress,
                contactInfo.StreetAddress2,
                contactInfo.City,
                contactInfo.State,
                contactInfo.ZIP,
                comments.text.toString(),
                "en-US",
                "Y"
            )
        return newModel
    }

    private fun wasPrerecorded_GovModel():String{
        return  if(wasPrerecorded.checkedRadioButtonId == R.id.report_form_wasPrerecorded_radio_yes) "Y"
                else if(wasPrerecorded.checkedRadioButtonId == R.id.report_form_wasPrerecorded_radio_no) "N"
                else ""
    }

    private fun isMobileCall_GovModel():String{
        return  if(isMobileCall.checkedRadioButtonId == R.id.report_form_isMobileCall_radio_yes) "Y"
                else if(isMobileCall.checkedRadioButtonId == R.id.report_form_isMobileCall_radio_no) "N"
                else ""
    }

    private fun haveDoneBusiness_GovModel():String{
        return  if(haveDoneBusiness.checkedRadioButtonId == R.id.report_form_haveDoneBusiness_radio_yes) "Y"
                else if(haveDoneBusiness.checkedRadioButtonId == R.id.report_form_haveDoneBusiness_radio_no) "N"
                else ""
    }

    private fun askedToStop_GovModel():String{
        return  if(askedToStop.checkedRadioButtonId == R.id.report_form_askedToStop_radio_yes) "Y"
                else if(askedToStop.checkedRadioButtonId == R.id.report_form_askedToStop_radio_yes) "N"
                else ""
    }

}