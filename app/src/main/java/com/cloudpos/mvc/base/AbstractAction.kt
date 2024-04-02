//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import android.content.Context

abstract class AbstractAction {
    var mContext: Context? = null
    fun setContext(context: Context?) {
        mContext = context
    }

    open fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {}
    open fun doAfter(param: Map<String?, Any?>?, callback: ActionCallback?) {}
}