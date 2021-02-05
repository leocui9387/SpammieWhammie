package com.basicbear.spammiewhammie.Validation

import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DoNotCallApi {
    @GET("/js/json/canadianAreaCodes.json")
    fun fetchCanadianAreaCodes(): Call<String>

    @GET("/js/json/areaCodeStateMappings.json")
    fun fetchValidAreaCodes(): Call<String>

    @POST("/save-complaint")
    fun postSaveComplaint(@Body model:String): Call<String>

    @POST("/complaint-step2") //dnc_app.config.complaint_step2_url
    fun postStep1(@Body model:String):Call<String>

    @POST("/complaint-text-message-link")//dnc_app.config.complaint_text_url
    fun postStep1CA():Call<String>
}