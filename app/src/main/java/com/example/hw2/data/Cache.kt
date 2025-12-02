package com.example.hw2.data

import android.content.Context
import com.google.gson.Gson
import java.io.File

object DiskCache {
    private const val CACHE_FILE = "api_cache.json"
    private val gson = Gson()

    fun saveResponse(context: Context, data: ResponseData) {
        try {
            val file = File(context.cacheDir, CACHE_FILE)
            file.writeText(gson.toJson(data))
        } catch (_: Exception) {}
    }

    fun loadResponse(context: Context): ResponseData? {
        return try {
            val file = File(context.cacheDir, CACHE_FILE)
            if (!file.exists()) return null

            val json = file.readText()
            gson.fromJson(json, ResponseData::class.java)
        } catch (e: Exception) {
            null
        }
    }
}