package com.cloudpos.androidmvcmodel

import android.app.Application
import android.content.Context
import android.util.Log
import com.cloudpos.androidmvcmodel.entity.MainItem
import com.cloudpos.androidmvcmodel.helper.LanguageHelper
import com.cloudpos.androidmvcmodel.helper.TerminalHelper
import com.cloudpos.androidmvcmodel.helper.XmlPullParserHelper
import com.cloudpos.mvc.base.ActionManager
import com.cloudpos.mvc.impl.ActionContainerImpl
import java.util.*

class MainApplication : Application() {
    private var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        initParameter()
        ActionManager.Companion.initActionContainer(ActionContainerImpl(context))
    }

    private fun initParameter() {
        context = this
        testItems = XmlPullParserHelper.getTestItems(context as MainApplication, TerminalHelper.terminalType)
        for (mainItem in testItems) {
            Log.e("DEBUG", "" + mainItem.getDisplayName(LanguageHelper.getLanguageType(context)))
        }
    }

    companion object {
        var testItems: MutableList<MainItem> = ArrayList()
    }
}