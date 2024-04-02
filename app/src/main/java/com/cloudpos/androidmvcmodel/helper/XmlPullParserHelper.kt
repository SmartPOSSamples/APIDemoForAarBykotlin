package com.cloudpos.androidmvcmodel.helper

import android.content.Context
import android.util.Log
import com.cloudpos.androidmvcmodel.common.Constants
import com.cloudpos.androidmvcmodel.entity.MainItem
import com.cloudpos.androidmvcmodel.entity.SubItem
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.util.*

object XmlPullParserHelper {
    @Throws(XmlPullParserException::class)
    fun getXmlPullParser(context: Context, terminalType: Int): XmlPullParser {
        var xmlPullParser = XmlPullParserFactory.newInstance().newPullParser()
        xmlPullParser = when (terminalType) {
            TerminalHelper.TERMINAL_TYPE_WIZARHAND_Q1 -> context.resources.getXml(R.xml.wizarhand_q1)
            TerminalHelper.TERMINAL_TYPE_WIZARPAD_1 -> context.resources.getXml(R.xml.wizarpad_1)
            TerminalHelper.TERMINAL_TYPE_WIZARHAND_M0 -> context.resources.getXml(R.xml.wizarhand_m0)
            else -> context.resources.getXml(R.xml.wizarpos_1)
        }
        return xmlPullParser
    }

    fun parseToMainItem(xmlPullParser: XmlPullParser): MainItem {
        val mainItem = MainItem()
        val nameCN = xmlPullParser.getAttributeValue(null, "name_CN")
        val nameEN = xmlPullParser.getAttributeValue(null, "name_EN")
        val command = xmlPullParser.getAttributeValue(null, "command")
        val isActivity = xmlPullParser.getAttributeValue(null, "isActivity")
        mainItem.setDisplayNameCN(nameCN)
        mainItem.setDisplayNameEN(nameEN)
        mainItem.command = command
        if (isActivity != null && isActivity == "true") {
            mainItem.setActivity(true)
        } else {
            mainItem.setActivity(false)
        }
        return mainItem
    }

    fun parseToSubItem(xmlPullParser: XmlPullParser): SubItem {
        val subItem = SubItem()
        val nameCN = xmlPullParser.getAttributeValue(null, "name_CN")
        val nameEN = xmlPullParser.getAttributeValue(null, "name_EN")
        val command = xmlPullParser.getAttributeValue(null, "command")
        val needTest = xmlPullParser.getAttributeValue(null, "needTest")
        subItem.setDisplayNameCN(nameCN)
        subItem.setDisplayNameEN(nameEN)
        subItem.command = command
        if (needTest != null && needTest == "true") {
            subItem.isNeedTest = true
        } else {
            subItem.isNeedTest = false
        }
        return subItem
    }

    fun getTestItems(context: Context, terminalType: Int): MutableList<MainItem> {
        Log.d("DEBUG", "getTestItems")
        val testItems: MutableList<MainItem> = ArrayList()
        try {
            val xmlPullParser = getXmlPullParser(context,
                    terminalType)
            var mEventType = xmlPullParser.eventType
            var mainItem = MainItem()
            var tagName: String? = null
            while (mEventType != XmlPullParser.END_DOCUMENT) {
                if (mEventType == XmlPullParser.START_TAG) {
                    tagName = xmlPullParser.name
                    if (tagName == Constants.MAIN_ITEM) {
                        mainItem = parseToMainItem(xmlPullParser)
                        //                        Log.d("DEBUG", "" + mainItem.getDisplayName(1));
                    } else if (tagName == Constants.SUB_ITEM) {
                        val subItem = parseToSubItem(xmlPullParser)
                        mainItem!!.addSubItem(subItem)
                    }
                } else if (mEventType == XmlPullParser.END_TAG) {
                    tagName = xmlPullParser.name
                    if (tagName == Constants.MAIN_ITEM) {
                        testItems.add(mainItem)
                    }
                }
                mEventType = xmlPullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return testItems
    }
}