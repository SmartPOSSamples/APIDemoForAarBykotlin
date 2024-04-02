package com.cloudpos.mvc.impl

import android.content.Context
import android.util.Log
import com.cloudpos.androidmvcmodel.MainApplication
import com.cloudpos.androidmvcmodel.entity.MainItem
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.mvc.base.AbstractAction
import com.cloudpos.mvc.base.ActionContainer

class ActionContainerImpl(private val context: Context?) : ActionContainer() {
    override fun initActions() {
        for (mainItem in MainApplication.Companion.testItems) {
            try {
                val classPath = getClassPath(mainItem)
                val clazz = Class.forName(classPath)
                if (mainItem != null) {
                    addAction(mainItem.command, clazz as Class<out AbstractAction>, true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Can't find this action")
            }
        }
    }

    private fun getClassPath(mainItem: MainItem): String? {
        var classPath: String? = null
        classPath = if (mainItem.isUnique()) {
            mainItem.packageName
        } else {
            (context!!.resources.getString(R.string.action_package_name)
                    + mainItem.command)
        }
        return classPath
    }

    companion object {
        private const val TAG = "ActionContainerImpl"
    }
}