package com.basicbear.spammiewhammie.api

import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface DoNotCallApi {
    @GET("/js/json/canadianAreaCodes.json")
    fun fetchCanadianAreaCodes(): Call<String>

    @GET("/js/json/areaCodeStateMappings.json")
    fun fetchValidAreaCodes(): Call<String>

    @POST("/save-complaint")
    fun postSaveComplaint(): Call<ResponseBody>
}