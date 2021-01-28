package com.basicbear.spammiewhammie.ui.contact_info


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Parcel
import android.os.Parcelable
import android.telephony.TelephonyManager
import android.util.Log
import com.google.gson.Gson
import java.io.*

private const val TAG = "PersonalInfo object"
private const val fileName = "contact_info_file.txt"
private const val PERMISSIONS_REQUEST_READ_PHONE_STATE = 10

data class PersonalInfo (
    var MyPhoneNumber:String ="",
    var FirstName:String="",
    var LastName:String = "",
    var StreetAddress:String ="",
    var StreetAddress2:String ="",
    var City:String ="",
    var State:String = "",
    var ZIP:String =""
        ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"deparcel failed",
        parcel.readString()?:"deparcel failed",
        parcel.readString()?:"deparcel failed",
        parcel.readString()?:"deparcel failed",
        parcel.readString()?:"deparcel failed",
        parcel.readString()?:"deparcel failed",
        parcel.readString()?:"deparcel failed"
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(MyPhoneNumber)
        parcel.writeString(FirstName)
        parcel.writeString(LastName)
        parcel.writeString(StreetAddress)
        parcel.writeString(StreetAddress2)
        parcel.writeString(City)
        parcel.writeString(State)
        parcel.writeString(ZIP)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getContactInfo(activity: Activity){
        var infoBuff:PersonalInfo
        var dataJSON: String
        try {
            val fis: FileInputStream = activity.openFileInput(fileName)
            val r = BufferedReader(InputStreamReader(fis))
            dataJSON = r.readText()
            r.close()
        } catch (e: IOException) {
            Log.i(TAG, "file read failed")
            return
        }

        var gson: Gson = Gson()
        infoBuff = gson.fromJson(dataJSON,PersonalInfo::class.java)

        this.MyPhoneNumber =  infoBuff.MyPhoneNumber
        this.FirstName =  infoBuff.FirstName
        this.LastName =  infoBuff.LastName
        this.StreetAddress =  infoBuff.StreetAddress
        this.StreetAddress2 =  infoBuff.StreetAddress2
        this.City =  infoBuff.City
        this.State =  infoBuff.State
        this.ZIP =  infoBuff.ZIP

    }

    fun saveToFile(activity: Activity){

        try{
            var gson: Gson = Gson()
            var infoBuff:String = gson.toJson(this)

            val fos: FileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE)
            val out: Writer = OutputStreamWriter(fos)

            out.write(infoBuff)
            out.close()
        }catch (e: Exception){
            Log.d(TAG, " create file failed:" + e)
        }

    }

    fun GetThisPhoneNumber(activity: Activity){
        if(!MyPhoneNumber.isEmpty()) return

        if(activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
        }

            val tMgr: TelephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            MyPhoneNumber = tMgr.line1Number?:""
            saveToFile(activity)

    }

    fun getFormattedPhoneNumber():String{
        val numOnly:Regex = Regex("[^0-9]")
        return MyPhoneNumber.replace(numOnly, "")
    }

    companion object CREATOR : Parcelable.Creator<PersonalInfo> {
        override fun createFromParcel(parcel: Parcel): PersonalInfo {
            return PersonalInfo(parcel)
        }

        override fun newArray(size: Int): Array<PersonalInfo?> {
            return arrayOfNulls(size)
        }
    }

}