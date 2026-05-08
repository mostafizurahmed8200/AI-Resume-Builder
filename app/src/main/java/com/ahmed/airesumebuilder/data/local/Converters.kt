package com.ahmed.airesumebuilder.data.local

import androidx.room.TypeConverter
import com.ahmed.airesumebuilder.domain.model.Education
import com.ahmed.airesumebuilder.domain.model.PersonalInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    //Personal Info Converter
    @TypeConverter
    fun fromPersonalInfo(value: PersonalInfo): String = gson.toJson(value)

    @TypeConverter
    fun toPersonalInfo(value: String): PersonalInfo = gson.fromJson(value, PersonalInfo::class.java)

    //Education Info Converter
    @TypeConverter
    fun fromEducationList(value: List<Education>): String = gson.toJson(value)

    @TypeConverter
    fun toEducationList(value: String): List<Education> {
        val type = object : TypeToken<List<Education>>() {}.type
        return gson.fromJson(value, type)
    }





}