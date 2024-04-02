package com.cloudpos.androidmvcmodel.helper

import android.util.Log

object SystemPropertyHelper {
    operator fun get(propertyName: String): String {
        var property: Any? = null
        try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            Log.i("systemProperties", systemProperties.toString())
            property = systemProperties.getMethod("get", *arrayOf<Class<*>>(
                    String::class.java, String::class.java
            )).invoke(systemProperties, *arrayOf<Any>(
                    propertyName, "unknown"
            ))
            Log.i("bootloaderVersion", property.javaClass.toString())
        } catch (e: Exception) {
            property = ""
            e.printStackTrace()
        }
        return property.toString()
    }
}