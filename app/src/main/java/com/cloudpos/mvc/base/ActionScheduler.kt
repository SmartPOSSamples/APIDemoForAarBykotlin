//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import com.cloudpos.mvc.common.Logger
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class ActionScheduler : Thread() {
    private val mActionQueue: LinkedBlockingQueue<ActionContext?> = LinkedBlockingQueue<ActionContext?>(20)
    private val service = Executors.newFixedThreadPool(30)
    override fun run() {
        var mContext: ActionContext? = null
        while (true) {
            while (true) {
                try {
                    mContext = mActionQueue.take()
                    service.submit(mContext)
                } catch (var3: Exception) {
                    Logger.error("调度器发生错误", var3)
                }
            }
        }
    }

    fun setActionContext(context: ActionContext?) {
        if (context != null) {
            try {
                mActionQueue.put(context)
            } catch (var3: InterruptedException) {
                var3.printStackTrace()
            }
        }
    }

    companion object {
        val instance = ActionScheduler()
    }
}