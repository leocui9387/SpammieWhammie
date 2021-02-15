package com.basicbear.spammiewhammie.ui.gov

import android.util.Log
import android.webkit.JavascriptInterface
import com.basicbear.spammiewhammie.Validation.GovModel
import com.google.gson.Gson

private val TAG = "GovModelJsInterface"

class GovModelJsInterface() {

    lateinit var submitData: GovModel
    lateinit var result: String

    @JavascriptInterface
    fun passModel(model:String, responseBody:String){

        val gson = Gson()
        val modelBuff = gson.fromJson(model, GovModel::class.java)

        submitData =    if(modelBuff !=null) modelBuff
                        else GovModel("")

        result = responseBody

        Log.d(TAG,"stuff: " + model)
        Log.d(TAG,"result: " + result)
    }

    private fun add2Database(model:GovModel){

    }
}