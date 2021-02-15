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
    var MyPhoneNumberOverride:Boolean = false,
    var MyEmail:String="",
    var FirstName:String="",
    var LastName:String = "",
    var StreetAddress:String ="",
    var StreetAddress2:String ="",
    var City:String ="",
    var State:String = "",
    var ZIP:String =""
        ):Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readByte() != 0.toByte(),
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"",
            parcel.readString()?:"") {
    }

    fun getContactInfo(activity: Activity):Boolean{
        var infoBuff:PersonalInfo
        var dataJSON: String
        try {
            val fis: FileInputStream = activity.openFileInput(fileName)
            val r = BufferedReader(InputStreamReader(fis))
            dataJSON = r.readText()
            r.close()

        } catch (e: IOException) {
            Log.i(TAG, "file read failed")
            return false
        }

        var gson: Gson = Gson()
        infoBuff = gson.fromJson(dataJSON,PersonalInfo::class.java)

        this.MyPhoneNumber =  infoBuff.MyPhoneNumber
        this.MyPhoneNumberOverride =  infoBuff.MyPhoneNumberOverride
        this.MyEmail = infoBuff.MyEmail
        this.FirstName =  infoBuff.FirstName
        this.LastName =  infoBuff.LastName
        this.StreetAddress =  infoBuff.StreetAddress
        this.StreetAddress2 =  infoBuff.StreetAddress2
        this.City =  infoBuff.City
        this.State =  infoBuff.State
        this.ZIP =  infoBuff.ZIP

        return true
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
        if(MyPhoneNumberOverride) return

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

    fun validate():String{

        MyPhoneNumber = numbersOnly(MyPhoneNumber)
        if( MyPhoneNumber.length >= 10 ) return "Your phone number must have at least 10 digits."

        val stateCodes:List<String> = "AL,AK,AZ,AR,CA,CO,CT,DE,FL,GA,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,MS,MO,MT,NE,NV,NH,NJ,NM,NY,NC,ND,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VT,VA,WA,WV,WI,WY,DC,GU,MH,MP,PR,VI,AE,AA,AP".split(",")
        if( stateCodes.contains(State) ) return "Your state code is invalid."

        return ""
    }

    fun isEmpty():Boolean{
        return MyPhoneNumber.isEmpty() || MyEmail.isEmpty() || FirstName.isEmpty() || LastName.isEmpty()|| StreetAddress.isEmpty()|| City.isEmpty()|| State.isEmpty() || ZIP.isEmpty()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(MyPhoneNumber)
        parcel.writeByte(if (MyPhoneNumberOverride) 1 else 0)
        parcel.writeString(MyEmail)
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

    fun numbersOnly(original: String): String {
        val numOnly: Regex = Regex("[^0-9]")
        return original.replace(numOnly, "")
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