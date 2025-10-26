package com.codeleg.cashflow.database

import androidx.room.TypeConverter
import java.util.Date

class Convertors {
    @TypeConverter
    fun fromTimeStamp(value:Long?): Date?{
        return value?.let { Date(it) }
    }


    @TypeConverter
    fun dateToTimestamp(date: Date?):Long?{
        return date?.time
    }

}