package com.ahmed.airesumebuilder.data.local

import androidx.room.TypeConverter
import com.ahmed.airesumebuilder.domain.model.Education
import com.ahmed.airesumebuilder.domain.model.Experience
import com.ahmed.airesumebuilder.domain.model.PersonalInfo
import com.ahmed.airesumebuilder.domain.model.Project
import com.ahmed.airesumebuilder.domain.model.Skill
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

    //Experiences Type Converter
    @TypeConverter
    fun fromExperienceList(value: List<Experience>): String = gson.toJson(value)

    @TypeConverter
    fun toExperienceList(value: String): List<Experience> {
        val type = object : TypeToken<List<Experience>>() {}.type
        return gson.fromJson(value, type)
    }

    //Skills Type Converter
    @TypeConverter
    fun fromSkillsList(value: List<Skill>): String = gson.toJson(value)

    @TypeConverter
    fun toSkillsList(value: String): List<Skill> {
        val type = object : TypeToken<List<Skill>>() {}.type
        return gson.fromJson(value, type)
    }

    //Project Type Converter
    @TypeConverter
    fun fromProjectList(value: List<Project>): String = gson.toJson(value)

    @TypeConverter
    fun toProjectList(value: String): List<Project> {
        val type = object : TypeToken<List<Project>>() {}.type
        return gson.fromJson(value, type)
    }

    //String list type converter
    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

}