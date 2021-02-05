package com.basicbear.spammiewhammie.Validation


import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


private val TAG = "AreaCodes Object"
private val CanadianAreaCode_Error = "[-1]"
private val ValidAreaCode_Error = "[{\"code\":\"000\",\"state\":\"ZZ\"}]"


class AreaCodes(
        private var dncApi: DoNotCallApi
) {

        init {
            updateCodes()
        }

        private lateinit var validAreaCodes: Map<Int,String>
        private lateinit var canadianAreaCodes: Array<Int>
        private lateinit var codeCheckDate: LocalDate

        private fun updateCodes(){

            if (!this::codeCheckDate.isInitialized || codeCheckDate.plusDays(1).isBefore(LocalDate.now())){
                Log.d(TAG,"Getting valid and canadian area codes")
                val gson = Gson()

                getValidAreaCodes(gson)
                getCanadianAreaCodes(gson)
            }
        }

    private fun getValidAreaCodes(gson: Gson){
        val validAreaCodesCall = dncApi.fetchValidAreaCodes()
        validAreaCodesCall.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val data = response.body()?:ValidAreaCode_Error
                val structuredData =  gson.fromJson(data, Array<validCodes>::class.java)

                validAreaCodes = object: HashMap<Int,String>(){
                    init {
                        for (code in structuredData){
                            put(code.code.toInt(),code.state)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                validAreaCodes = mapOf<Int,String>()
            }

        })
    }

    private fun getCanadianAreaCodes(gson: Gson){
        val canadianAreaCodesCall = dncApi.fetchCanadianAreaCodes()
        canadianAreaCodesCall.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                val data = response.body()?:CanadianAreaCode_Error
                val array =  gson.fromJson(data, Array<String>::class.java)[0].split(',')
                canadianAreaCodes =  array.map(){x -> x.toInt()} .toTypedArray()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                canadianAreaCodes= emptyArray()
            }
        })
    }

        fun isValid(areaCode:Int):Boolean{
            return validAreaCodes.containsKey(areaCode)
        }

        fun isCanadian(areaCode:Int):Boolean{

            return canadianAreaCodes.contains(areaCode)
        }

    private inner class validCodes{
        lateinit var code:String
        lateinit var state:String
    }

}