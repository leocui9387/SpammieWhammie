package com.basicbear.spammiewhammie.database

import androidx.room.TypeConverter
import java.util.*

class ReportTypeConverters {

    @TypeConverter
    fun toUUID(uuid:String?): UUID?{
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?):String?{
        return uuid?.toString()
    }

}