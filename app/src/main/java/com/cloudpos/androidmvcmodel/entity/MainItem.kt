package com.cloudpos.androidmvcmodel.entity

import java.util.*

class MainItem : TestItem() {
    private var isActivity = false
    private val subItems: MutableList<SubItem> = ArrayList()
    var packageName: String? = null
    private var isUnique = false


    fun isActivity(): Boolean {
        return isActivity
    }

    fun setActivity(isActivity: Boolean) {
        this.isActivity = isActivity
    }

    fun getSubItem(position: Int): SubItem {
        return subItems[position]
    }

    fun getSubItems(): List<SubItem> {
        return subItems
    }

    val testSubItems: List<SubItem>
        get() {
            val testSubItems: MutableList<SubItem> = ArrayList()
            for (subItem in subItems) {
                if (subItem.isNeedTest) {
                    testSubItems.add(subItem)
                }
            }
            return testSubItems
        }

    fun addSubItem(subItem: SubItem) {
        subItems.add(subItem)
    }


    fun isUnique(): Boolean {
        return isUnique
    }

    fun setUnique(isUnique: Boolean) {
        this.isUnique = isUnique
    }

}