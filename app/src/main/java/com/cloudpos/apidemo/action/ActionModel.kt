package com.cloudpos.apidemo.action

import com.cloudpos.androidmvcmodel.common.Constants
import com.cloudpos.mvc.base.AbstractAction
import com.cloudpos.mvc.base.ActionCallback

open class ActionModel : AbstractAction() {
    protected var mCallback: ActionCallback? = null
    fun setParameter(callback: ActionCallback?) {
        if (mCallback == null) {
            mCallback = callback
        }
    }

    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        setParameter(callback)
    }

    fun sendSuccessLog(log: String) {
//        StackTraceElement[] element = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stackTraceElement : element) {
//            Log.e("DEBUG", "method: " + stackTraceElement.getMethodName());
//        }
        mCallback!!.sendResponse(Constants.HANDLER_LOG_SUCCESS, "\t\t"
                + Thread.currentThread().stackTrace[3].methodName + "\t" + log)
    }

    fun sendSuccessLog2(log: String) {
//        StackTraceElement[] element = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stackTraceElement : element) {
//            Log.e("DEBUG", "method: " + stackTraceElement.getMethodName());
//        }
        mCallback!!.sendResponse(Constants.HANDLER_LOG_SUCCESS, "\t\t" + log)
    }

    fun sendFailedLog(log: String) {
//        StackTraceElement[] element = Thread.currentThread().getStackTrace();
//        for (StackTraceElement stackTraceElement : element) {
//            Log.e("DEBUG", "method: " + stackTraceElement.getMethodName());
//        }
        mCallback!!.sendResponse(Constants.HANDLER_LOG_FAILED, "\t\t"
                + Thread.currentThread().stackTrace[3].methodName + "\t" + log)
    }

    fun sendFailedLog2(log: String) {
        mCallback!!.sendResponse(Constants.HANDLER_LOG_FAILED, "\t\t" + log)
    }

    fun sendNormalLog(log: String) {
        mCallback!!.sendResponse(Constants.HANDLER_LOG, "\t\t" + log)
    }
}