//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import android.content.Context
import com.cloudpos.androidmvcmodel.common.Constants
import com.cloudpos.mvc.common.Logger
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class ActionContext : Runnable {
    private var action: AbstractAction? = null
    private var context: Context? = null
    private var param: Map<String?, Any?>? = null
    private var callback: ActionCallback? = null
    var result: Any? = null
    private var actionUrl: String? = null
    private var methodName: String? = null
    private val resultLock = ReentrantLock()
    private val resultCondition: Condition
    private var hasReturn: Boolean
    override fun run() {
        if (action == null) {
            Logger.error("Not found action! Please initional ActionContain and register your Action Class")
        } else {
            if (callback == null) {
                Logger.warn("No call back")
            }
            try {
                resultLock.lock()
                action!!.setContext(context)
                action!!.doBefore(param, callback)
                this.invoke()
                action!!.doAfter(param, callback)
            } catch (var6: Exception) {
                val errorMsg = "Invoke method error: " + action!!.javaClass.name + "#" + methodName
                if (var6.cause == null) {
                    Logger.error(errorMsg, var6)
                } else if (var6.cause is UnknownHostException) {
                    Logger.error(errorMsg)
                    Logger.error(getStackTraceString(var6.cause))
                } else {
                    Logger.error(errorMsg, var6.cause)
                }
                callback!!.sendResponse(Constants.HANDLER_LOG_FAILED, var6.cause.toString())
            } finally {
                hasReturn = true
                resultCondition.signalAll()
                resultLock.unlock()
            }
        }
    }

//    fun getResult(): Any? {
//        resultLock.lock()
//        try {
//            if (!hasReturn) {
//                resultCondition.await()
//            }
//        } catch (var5: InterruptedException) {
//            var5.printStackTrace()
//        } finally {
//            resultLock.unlock()
//        }
//        return result
//    }

    @Throws(Exception::class)
    private operator fun invoke() {
        parseActionUrl()
        var callbackParam: Class<*> = ActionCallback::class.java
        if (callback != null) {
            callbackParam = callback!!.javaClass.superclass
        }
        val helper = BeanHelper(action)
        val method = helper.getMethod(methodName, *arrayOf(MutableMap::class.java, callbackParam))
        result = method!!.invoke(action, param, callback)
    }

    private fun parseActionUrl() {
        val index = actionUrl!!.indexOf("/")
        if (index == -1) {
            methodName = "execute"
        } else {
            methodName = actionUrl!!.substring(index + 1)
        }
    }

    fun setParam(param: Map<String?, Any?>?) {
        this.param = param
    }

    fun setCallback(callback: ActionCallback?) {
        this.callback = callback
    }

    fun setActionUrl(actionUrl: String?) {
        this.actionUrl = actionUrl
    }

    fun setContext(context: Context?) {
        this.context = context
    }

    fun setAction(action: AbstractAction?) {
        this.action = action
    }

    companion object {
        fun parseActionId(actionUrl: String): String {
            val index = actionUrl.indexOf("/")
            return if (index == -1) actionUrl else actionUrl.substring(0, index)
        }

        fun getStackTraceString(tr: Throwable?): String {
            return if (tr == null) {
                ""
            } else {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                tr.printStackTrace(pw)
                sw.toString()
            }
        }
    }

    init {
        resultCondition = resultLock.newCondition()
        hasReturn = false
    }
}