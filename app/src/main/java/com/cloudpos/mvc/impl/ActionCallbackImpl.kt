package com.cloudpos.mvc.impl

import android.content.Context
import android.os.Handler
import com.cloudpos.androidmvcmodel.common.Constants
import com.cloudpos.mvc.base.ActionCallback


class ActionCallbackImpl(override var context: Context?, private val handler: Handler) : ActionCallback() {
    override fun sendResponse(code: Int) {
        handler.obtainMessage(code).sendToTarget()
    }

    override fun sendResponse(code: Int, msg: String?) {
        handler.obtainMessage(code, """		$msg
""").sendToTarget()
    }

    override fun sendResponse(msg: String?) {
        sendResponse(Constants.HANDLER_LOG, msg)
    } //    public void sendLog(String log) {

    //        handler.obtainMessage(Constants.HANDLER_LOG, "\t\t" + log + "\n").sendToTarget();
//    }
//
//    public void sendSuccessLog(String successLog) {
//        handler.obtainMessage(Constants.HANDLER_LOG_SUCCESS, "\t\t" + successLog + "\n")
//                .sendToTarget();
//    }
//
//    public void sendFailedLog(String failedLog) {
//        handler.obtainMessage(Constants.HANDLER_LOG_FAILED, "\t\t" + failedLog + "\n")
//                .sendToTarget();
//    }
}
