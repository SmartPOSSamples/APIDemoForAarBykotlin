//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import android.content.Context
import android.os.Handler

abstract class ActionCallback {
    open var context: Context? = null

    open fun ActionCallback() {}

    open fun ActionCallback(context: Context?) {
        this.context = context
    }



    open fun callbackInHandler(methodName: String?, vararg args: Any?) {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                try {
                    BeanHelper(this).invoke(methodName, *args)
                } catch (var2: Exception) {
                    var2.printStackTrace()
                }
            }
        })
    }

    open fun sendResponse(code: Int) {}

    open fun sendResponse(msg: String?) {}

    open fun sendResponse(code: Int, msg: String?) {}
}