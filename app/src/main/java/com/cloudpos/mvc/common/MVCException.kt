//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.cloudpos.mvc.common

class MVCException : RuntimeException {
    constructor() {}
    constructor(msg: String?) : super(msg) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(msg: String?, cause: Throwable?) : super(msg, cause) {}

    companion object {
        private const val serialVersionUID = -5923896637917336703L
    }
}