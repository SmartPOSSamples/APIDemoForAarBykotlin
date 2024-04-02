package com.cloudpos.apidemo.common

import java.util.*

object Common {
    var testBytes = byteArrayOf(0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39)
    fun createMasterKey(length: Int): ByteArray {
        val array = ByteArray(length)
        for (i in 0 until length) {
//            array[i] = (byte) 0x38;
            array[i] = testBytes[Random().nextInt(testBytes.size)]
        }
        return array
    }

    fun transferErrorCode(errorCode: Int): Int {
        val a = -errorCode
        val b = a and 0x0000FF
        return -b
    }

    //        for (StackTraceElement stackTraceElement : eles) {
//            Log.e("stackTraceElement", stackTraceElement.getMethodName());
//        }
    val methodName: String
        get() {
            val eles = Thread.currentThread().stackTrace
            //        for (StackTraceElement stackTraceElement : eles) {
//            Log.e("stackTraceElement", stackTraceElement.getMethodName());
//        }
            return eles[5].methodName
        }
}