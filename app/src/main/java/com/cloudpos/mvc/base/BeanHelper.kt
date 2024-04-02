//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.base

import com.cloudpos.mvc.common.Logger
import java.lang.reflect.Method
import java.util.*

internal class BeanHelper {
    private var mClass: Class<*>
    private var mObject: Any? = null
    private lateinit var declaredMethods: Array<Method>

    constructor(clazz: Class<*>) {
        mClass = clazz
    }

    constructor(obj: Any?) {
        mObject = obj
        mClass = mObject!!.javaClass
    }

    @Throws(NoSuchMethodException::class)
    fun getMethod(methodName: String?, vararg classes: Class<*>): Method {
        declaredMethods = mClass.declaredMethods
        var result: Method? = null
        var matchLevel = -1
        var isFirst = true
        var var9: Array<Method>
        val var8 = declaredMethods.also { var9 = it }.size
        for (var7 in 0 until var8) {
            val method = var9[var7]
            val name = method.name
            if (name == methodName) {
                val paramTypes = method.parameterTypes
                if (paramTypes.size == classes.size) {
                    val tempMatchLevel = matchLevel(paramTypes, classes as Array<Class<*>>)
                    if (tempMatchLevel >= 0) {
                        if (isFirst && matchLevel < tempMatchLevel) {
                            isFirst = false
                            matchLevel = tempMatchLevel
                        } else {
                            if (matchLevel >= tempMatchLevel) {
                                continue
                            }
                            matchLevel = tempMatchLevel
                        }
                        result = method
                    }
                }
            }
        }
        return result ?: throw NoSuchMethodException(methodName + " " + Arrays.asList(*classes).toString())
    }

    fun getClosestClass(clazz: Class<*>): Class<*> {
        return clazz.superclass
    }

    fun matchLevel(paramTypes: Array<Class<*>>, transferParamTypes: Array<Class<*>>): Int {
        var matchLevel = -1
        for (m in paramTypes.indices) {
            val paramType = paramTypes[m]
            val tParamType = transferParamTypes[m]
            if (paramType == tParamType) {
                ++matchLevel
            } else {
                val superClasses = getAllSuperClass(tParamType)
                for (n in 1..superClasses.size) {
                    val superClass = superClasses[n - 1]
                    if (superClass != null && superClass != paramType) {
                        break
                    }
                    matchLevel += n
                }
            }
        }
        return matchLevel
    }

    @Throws(Exception::class)
    operator fun invoke(methodName: String?, vararg args: Any?): Any {
        val method = getMethod(methodName, *getClassTypes(*args as Array<out Any>)!!)
        return method.invoke(this, *args)
    }

    companion object {
        fun getClassTypes(vararg args: Any): Array<Class<*>>? {
            return if (args == null) {
                null
            } else {
//                val classes: Array<Class<*>> = arrayOfNulls(args.size)
//                for (i in 0 until args.size) {
//                    classes[i] = args[i].javaClass
//                }
                return null
            }
        }

        fun getAllSuperClass(clazz: Class<*>): List<Class<*>?> {
            val classes: ArrayList<Class<*>?> = ArrayList<Class<*>?>()
            var cla: Class<*>? = clazz
            do {
                cla = cla!!.superclass
                Logger.debug("class: $clazz, super class: $cla")
                if (cla != null) {
                    classes.add(cla)
                }
            } while ((cla == null || cla != Any::class.java) && cla != null)
            return classes
        }
    }
}