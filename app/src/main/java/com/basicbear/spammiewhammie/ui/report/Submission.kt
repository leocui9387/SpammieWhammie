package com.basicbear.spammiewhammie.ui.report

import android.content.Context
import android.widget.Toast
import com.basicbear.spammiewhammie.ui.contact_info.PersonalInfo
import com.basicbear.spammiewhammie.ui.main.PhoneCall
import java.time.Instant

class Submission(
        private val context: Context,
        private val contactInfo:PersonalInfo,
        private val callInfo:PhoneCall
        ) {


    fun Validation(model: GovModel){

    }



    private fun step1LocalValidation(model: GovModel):Boolean{
        if(model.phoneNumber =="") {
            Toast.makeText(context,"Error: your phone number is empty", Toast.LENGTH_LONG).show()
            return false
        }
        if(model.dateOfCall ==""){
            Toast.makeText(context,"Error: your call date is empty", Toast.LENGTH_LONG).show()
            return false
        }

        if(!isDateValid(model.dateOfCall,model.wasPrerecorded)){


            Toast.makeText(context,"Error: your call date is empty", Toast.LENGTH_LONG).show()
            return false
        }
        if(isCanadianAreaCode(model.phoneNumber)){
            //js/json/canadianAreaCodes.json
            Toast.makeText(context,"Error: your phone number is Canadian", Toast.LENGTH_LONG).show()
            return false
        }
        if(!isValidAreaCode(model.phoneNumber)){
            //js/json/areaCodeStateMappings.json
            return false
        }

        return true
    }
    private fun isDateValid(date:String,isPreRecorded:String):Boolean{
        val cutOff = Instant.parse(
            if(isPreRecorded.equals("Y")) "2009-09-01"
            else "2003-10-01"
        )
        val inputDate= Instant.parse(date)

        if(inputDate > Instant.now() || inputDate < cutOff) return false

        return true
    }
    private fun isCanadianAreaCode(phoneNumber:String):Boolean{

    }

    private fun isValidAreaCode(phoneNumber: String):Boolean{

    }

    private fun step1ServerValidation(){

    }

    private fun step2LocalValidation(){

    }



}