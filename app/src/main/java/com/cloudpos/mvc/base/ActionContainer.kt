//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import com.cloudpos.mvc.common.Logger
import java.util.*

abstract class ActionContainer {
    protected var actions: HashMap<String?, Any?> = HashMap<String?, Any?>()
    abstract fun initActions()
    fun getActions(): Map<String?, Any?> {
        return actions
    }

    @Throws(Exception::class)
    private fun searchInstance(clazz: Class<out AbstractAction>): Any {
        val var3: Iterator<*> = actions.entries.iterator()
        var value: Any
        do {
            if (!var3.hasNext()) {
                return clazz.newInstance()
            }
            val entry: Map.Entry<*, *> = var3.next() as Map.Entry<*, *>
            value = entry.value!!
        } while (value == null || value.javaClass != clazz)
        return value
    }

    protected fun addAction(actionId: String?, clazz: Class<out AbstractAction>, singleton: Boolean = false): Boolean {
        if (singleton) {
            try {
                actions[actionId] = searchInstance(clazz)
            } catch (var5: Exception) {
                Logger.error("build singleton instance occur an error:", var5)
                return false
            }
        } else {
            actions[actionId] = clazz
        }
        return true
    }

    protected fun addAction(clazz: Class<out AbstractAction>, singleton: Boolean = false): Boolean {
        return this.addAction(clazz.name, clazz, singleton)
    }
}