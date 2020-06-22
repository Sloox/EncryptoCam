package com.example.encryptocam.commons.extensions

import android.util.Log
import com.example.encryptocam.BuildConfig

/**
 * Simplified loggin statements. For larger applications better to replace with something like timber
 * */
fun Any.logd(txt: String, enabled: Boolean = true, t: String = this::class.java.simpleName) { //internal debug logging
    if (BuildConfig.DEBUG && enabled) {
        val tag = if (!t.toLowerCase().contains(BuildConfig.APPLICATION_ID.toLowerCase())) BuildConfig.APPLICATION_ID + " " + t else t
        Log.d(tag, txt)
    }
}

fun Any.loge(txt: String, enabled: Boolean = true, t: String = this::class.java.simpleName) { //internal error logging
    if (BuildConfig.DEBUG && enabled) {
        val tag = if (!t.toLowerCase().contains(BuildConfig.APPLICATION_ID.toLowerCase())) BuildConfig.APPLICATION_ID + " " + t else t
        Log.e(tag, txt)
    }
}

fun Any.logi(txt: String, enabled: Boolean = true, t: String = this::class.java.simpleName) { //internal error logging
    if (BuildConfig.DEBUG && enabled) {
        val tag = if (!t.toLowerCase().contains(BuildConfig.APPLICATION_ID.toLowerCase())) BuildConfig.APPLICATION_ID + " " + t else t
        Log.i(tag, txt)
    }
}

fun Any.logw(txt: String, enabled: Boolean = true, t: String = this::class.java.simpleName) { // logwarn
    if (BuildConfig.DEBUG && enabled) {
        val tag = if (!t.toLowerCase().contains(BuildConfig.APPLICATION_ID.toLowerCase())) BuildConfig.APPLICATION_ID + " " + t else t
        Log.w(tag, txt)
    }
}

