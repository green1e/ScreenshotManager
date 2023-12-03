package com.example.alleassignment.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ImageConverters {

    companion object {
        val gson: Gson = GsonBuilder().create()
    }

    @TypeConverter
    fun fromStringToLabels(value: String?): MutableList<String>? {
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun labelsToString(labels: MutableList<String>?): String? {
        val type = object : TypeToken<MutableList<String>>() {}.type
        return gson.toJson(labels, type)
    }
}