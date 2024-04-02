//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.common

import android.util.Log

object Logger {
    var level = 3
    fun debug(msg: String?) {
        if (level <= 3) {
            Log.d(createTag(), msg)
        }
    }

    fun debug(msg: String?, tr: Throwable?) {
        if (level <= 3) {
            Log.d(createTag(), msg, tr)
        }
    }

    fun info(msg: String?) {
        if (level <= 4) {
            Log.i(createTag(), msg)
        }
    }

    fun info(msg: String?, tr: Throwable?) {
        if (level <= 4) {
            Log.i(createTag(), msg, tr)
        }
    }

    fun warn(msg: String?) {
        if (level <= 5) {
            Log.w(createTag(), msg)
        }
    }

    fun warn(msg: String?, tr: Throwable?) {
        if (level <= 5) {
            Log.w(createTag(), msg, tr)
        }
    }

    fun error(msg: String?) {
        if (level <= 6) {
            Log.e(createTag(), msg)
        }
    }

    fun error(msg: String?, tr: Throwable?) {
        if (level <= 6) {
            Log.e(createTag(), msg, tr)
        }
    }

    private fun createTag(): String? {
        val sts = Thread.currentThread().stackTrace
        return if (sts == null) {
            null
        } else {
            val var3 = sts.size
            for (var2 in 0 until var3) {
                val st = sts[var2]
                if (!st.isNativeMethod && st.className != Thread::class.java.name && st.className != Logger::class.java.name) {
                    return st.lineNumber.toString() + ":" + st.fileName
                }
            }
            ""
        }
    }
}