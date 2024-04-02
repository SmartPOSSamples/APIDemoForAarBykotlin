package com.cloudpos.androidmvcmodel.entity

import com.cloudpos.androidmvcmodel.helper.LanguageHelper

open class TestItem {
    var command: String? = null
    private var displayNameCN: String? = null
    private var displayNameEN: String? = null


    fun getDisplayName(languageType: Int): String? {
        return if (languageType == LanguageHelper.LANGUAGE_TYPE_CN) {
            displayNameCN
        } else {
            displayNameEN
        }
    }

    fun setDisplayNameCN(displayNameCN: String?) {
        this.displayNameCN = displayNameCN
    }

    fun setDisplayNameEN(displayNameEN: String?) {
        this.displayNameEN = displayNameEN
    }

    override fun toString(): String {
        return String.format("command = %s, displayCN = %s, displyEN = %s", command, displayNameCN,
                displayNameEN)
    }
}