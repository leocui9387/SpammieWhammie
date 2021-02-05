package com.basicbear.spammiewhammie.Validation

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val TAG = "ValidationObject"

class Validation(
        private val context: Context,
        private var finalModel:GovModel,
        private var dncApi: DoNotCallApi,
        private val areaCodes: AreaCodes
        ) {

    companion object {
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(Locale.ENGLISH)
    }

    fun validate():String{

        var message = step1LocalValidation()
        if(!message.isEmpty()) return message

        message = finalLocalValidation()
        //if(!message.isEmpty()) return message

        submitComplaint()
        return ""
    }

    private fun step1LocalValidation():String{

        if(finalModel.phoneNumber =="") {
            return "Error: your phone number is empty"
        }

        if(finalModel.dateOfCall == ""){
            return "Error: your call date is empty"
        }

        if(!isDateValid(finalModel.dateOfCall,finalModel.wasPrerecorded)){
            return "Error: your call date is invalid. General Cutoff: 10-1-2003 ,Prerecorded Cutoff: 9-1-2009"
        }

        val areaCode = getAreaCode(finalModel.phoneNumber)

        // Log.d(TAG,finalModel.phoneNumber + "|area code|" + areaCode)
        if(areaCodes.isCanadian(areaCode)){
            //js/json/canadianAreaCodes.json

            return "Error: your phone number is Canadian"
        }

        if(!areaCodes.isValid(areaCode)){
            //js/json/areaCodeStateMappings.json
            return "Error: your area code is invalid"
        }

        return ""
    }

    private fun isDateValid(date:String,isPreRecorded:String):Boolean{
        val cutOff =
            if(isPreRecorded.equals("Y")) LocalDate.of(2009,9,1)
            else LocalDate.of(2003,10,1)

        val inputDate= LocalDate.parse(date,dateFormatter)

        if(inputDate > LocalDate.now() || inputDate < cutOff) return false

        return true
    }

    private fun getAreaCode(phoneNumber:String):Int{
        val end = phoneNumber.length - 7
        val start = if(end < 3) 0 else end-3
        return phoneNumber.substring(start,end).toInt()
    }
/*
    Web app doesn't actually use the Step 1 validation
    private fun step1ServerValidation(){
        var e = GovE()
        e.DateOfCall = finalModel.dateOfCall
        e.PhoneNumber = finalModel.phoneNumber
        val gson = Gson()
        val stringy = gson.toJson(e)
        val step1test = dncApi.postStep1(stringy)
        Log.d(TAG,"Step 1 server validation : e value " + stringy)
        val response = step1test.enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

                //step2LocalValidation()
                Log.d(TAG,response.body()?:"Error step 1 response body")

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"Step 1 server test failed",Toast.LENGTH_LONG).show()
            }

        })

    }

 */

    fun finalLocalValidation():String{
        if(!(finalModel.wasPrerecorded.equals("Y")|| finalModel.wasPrerecorded.equals("N")))
            return  " was not answered."


        return ""
    }

    private fun submitComplaint(){
        val gson = Gson()
        val stringy = "{\"phoneNumber\":\"9197936255\",\"dateOfCall\":\"01/30/2021\",\"timeOfCall\":\"12\",\"minuteOfCall\":\"12\",\"wasPrerecorded\":\"Y\",\"isMobileCall\":\"\",\"subjectMatter\":\"Unknown\",\"companyPhoneNumber\":\"2025432028\",\"companyName\":\"unknown\",\"haveDoneBusiness\":\"N\",\"askedToStop\":\"N\",\"firstName\":\"Leo\",\"lastName\":\"Cui\",\"streetAddress1\":\"7701 Woodmont Ave Apt 404\",\"streetAddress2\":\"\",\"city\":\"Bethesda\",\"state\":\"MD\",\"zip\":\"20814\",\"comments\":\"was some prerecorded chinese dialog.\",\"language\":\"en-US\",\"validFlag\":\"Y\"}"//gson.toJson(finalModel)
        Log.d(TAG,"Final Model stringified: " + stringy)

        val saveComplaint = dncApi.postSaveComplaint(stringy)

        val response = saveComplaint.enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

                Toast.makeText(context,"Success! ",Toast.LENGTH_LONG).show()
                Log.d(TAG,"success response: "+ response.body())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(context,"Save Complaint failed",Toast.LENGTH_LONG).show()
                Log.d(TAG,"fail throwable: "+ t.message)
            }

        })
    }

    private inner class GovE{
        var PhoneNumber:String =""
        var DateOfCall:String =""
    }

    private inner class GovECA{
        var PhoneNumber:String =""
        var CallDate:String =""
    }
}