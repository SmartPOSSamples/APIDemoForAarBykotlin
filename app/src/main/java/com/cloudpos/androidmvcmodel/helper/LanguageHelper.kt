package com.cloudpos.androidmvcmodel.helper

import android.content.Context

object LanguageHelper {
    const val LANGUAGE_TYPE_OTHER = 0
    const val LANGUAGE_TYPE_CN = 1
    private const val LANGUAGE_TYPE_NONE = -1
    private var languageType = LANGUAGE_TYPE_NONE
    fun getLanguageType(context: Context?): Int {
        if (languageType == LANGUAGE_TYPE_NONE) {
            languageType = LANGUAGE_TYPE_OTHER
            val locale = context!!.resources.configuration.locale
            val language = locale.language
            if (language.endsWith("zh")) {
                languageType = LANGUAGE_TYPE_CN
            }
        }
        return languageType
    }
}