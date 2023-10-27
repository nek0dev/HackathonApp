package com.nekodev.hackathonapp.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IntListConverter: KoinComponent {
    private val gson: Gson by inject()

    @TypeConverter
    fun stringToObject(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Int>): String {
        return gson.toJson(list)
    }
}