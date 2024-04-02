//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import android.content.Context
import com.cloudpos.mvc.common.Logger
import com.cloudpos.mvc.common.MVCException
import java.util.*

class ActionManager {
    private val actionScheduler: ActionScheduler = ActionScheduler.Companion.instance
    protected var mActionContainer: HashMap<Any?, Any?> = HashMap<Any?, Any?>()
    private var isStart = false
    private fun start() {
        actionScheduler.start()
    }

    private fun newActionContext(actionUrl: String, context: Context?, param: Map<String?, Any?>?, callback: ActionCallback?): ActionContext? {
        val acontext = ActionContext()
        acontext.setActionUrl(actionUrl)
        acontext.setParam(param)
        acontext.setCallback(callback)
        if (acontext != null) {
            acontext.setContext(context)
        }
        setAction(actionUrl, acontext)
        actionScheduler.setActionContext(acontext)
        return acontext
    }

    private fun setAction(actionUrl: String, context: ActionContext?) {
        val actionId: String = ActionContext.Companion.parseActionId(actionUrl)
        val obj = mActionContainer[actionId]
        if (obj == null) {
            throw MVCException("Not found actionId in ActionContainer. The actionId is [$actionId].")
        } else {
            if (Class::class.java.isInstance(obj)) {
                try {
                    context!!.setAction((Class::class.java.cast(obj) as Class<*>).newInstance() as AbstractAction)
                } catch (var6: Exception) {
                    Logger.error("build instance error:", var6)
                }
            } else {
                context!!.setAction(obj as AbstractAction?)
            }
        }
    }

    companion object {
        private val actionManager = ActionManager()
        private val instance: ActionManager
            private get() {
                if (!actionManager.isStart) {
                    actionManager.start()
                    actionManager.isStart = true
                }
                return actionManager
            }

        fun initActionContainer(actions: ActionContainer) {
            actions.initActions()
            instance.mActionContainer.putAll(actions.getActions())
        }

        fun doSubmit(actionUrl: String, param: Map<String?, Any?>?, callback: ActionCallback?) {
            doSubmit(actionUrl, null as Context?, param, callback)
        }

        fun doSubmit(clazz: Class<out AbstractAction?>, methodName: String, param: Map<String?, Any?>?, callback: ActionCallback?) {
            doSubmit(clazz.name + "/" + methodName, param, callback)
        }

        fun doSubmit(actionUrl: String, context: Context?, param: Map<String?, Any?>?, callback: ActionCallback?) {
            instance.newActionContext(actionUrl, context, param, callback)
        }

        fun doSubmit(clazz: Class<out AbstractAction?>, methodName: String, context: Context?, param: Map<String?, Any?>?, callback: ActionCallback?) {
            doSubmit(clazz.name + "/" + methodName, context, param, callback)
        }

        fun <T> doSubmitForResult(actionUrl: String, param: Map<String?, Any?>?, callback: ActionCallback?): T? {
            return doSubmitForResult(actionUrl, null as Context?, param, callback)
        }

        fun <T> doSubmitForResult(clazz: Class<out AbstractAction?>, methodName: String, param: Map<String?, Any?>?, callback: ActionCallback?): T? {
            return doSubmitForResult(clazz.name + "/" + methodName, param, callback)
        }

        fun <T> doSubmitForResult(actionUrl: String, context: Context?, param: Map<String?, Any?>?, callback: ActionCallback?): T? {
            val acontext = instance.newActionContext(actionUrl, context, param, callback)
            return acontext!!.result as T
        }

        fun <T> doSubmitForResult(clazz: Class<out AbstractAction?>, methodName: String, context: Context?, param: Map<String?, Any?>?, callback: ActionCallback?): T? {
            return doSubmitForResult(clazz.name + "/" + methodName, context, param, callback)
        }
    }
}