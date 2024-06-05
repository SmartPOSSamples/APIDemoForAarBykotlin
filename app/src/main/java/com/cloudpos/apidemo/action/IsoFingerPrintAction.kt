package com.cloudpos.apidemo.action

import android.os.RemoteException
import android.util.Log
import com.cloudpos.*
import com.cloudpos.androidmvcmodel.common.Constants
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.fingerprint.*
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.sdk.util.SystemUtil
import java.util.*

/**
 * create by rf.w 19-2-28 10:24AM
 */
class IsoFingerPrintAction : ActionModel() {
    private var device: FingerprintDevice? = null
    private var userID = 9
    private val timeout = 60 * 1000
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext).getDevice("cloudpos.device.fingerprint") as FingerprintDevice
            if (SystemUtil.getProperty("wp.fingerprint.model").equals("aratek", ignoreCase = true)) {
                ISOFINGERPRINT_TYPE_ISO2005 = 0
            }
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.open(FingerprintDevice.ISO_FINGERPRINT)
            //清除指纹数据
            device!!.delAllFingers()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listenForFingerprint(param: Map<String?, Any?>?, callback: ActionCallback?) {
        sendSuccessLog("")
        try {
            val listener = OperationListener { arg0 ->
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    sendSuccessLog2(mContext!!.getString(R.string.scan_fingerprint_success))
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.scan_fingerprint_fail))
                }
            }
            device!!.listenForFingerprint(listener, TimeConstants.FOREVER)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun waitForFingerprint(param: Map<String?, Any?>?, callback: ActionCallback?) {
        sendSuccessLog("")
        try {
            val operationResult = device!!.waitForFingerprint(TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.scan_fingerprint_success))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.scan_fingerprint_fail))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun cancelRequest(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.cancelRequest()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun enroll(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
            userID++
            device!!.enroll(userID, timeout)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listenForEnroll(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            device!!.listenForEnroll({ operationResult ->
                if (operationResult is FingerprintPressOperationResult) {
                    callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
                } else if (operationResult is FingerprintRemoveOperationResult) {
                    callback.sendResponse(mContext!!.getString(R.string.remove_fingerprint))
                } else if (operationResult is FingerprintNoneOperationResult) {
                    callback.sendResponse(Constants.HANDLER_LOG_FAILED, mContext!!.getString(R.string.scan_fingerprint_fail) + ",retry") //重试
                } else if (operationResult is FingerprintTimeoutOperationResult) {
                    callback.sendResponse(Constants.HANDLER_LOG_FAILED, mContext!!.getString(R.string.enroll_timeout))
                } else if (operationResult is FingerprintOperationResult) {
                    val fingerprint = operationResult.getFingerprint(100, 100)
                    val feature = fingerprint.feature
                    if (feature != null) {
                        callback.sendResponse(Constants.HANDLER_LOG_SUCCESS, "finger.length=" + feature.size)
                    } else {
                        callback.sendResponse(Constants.HANDLER_LOG_SUCCESS, "finger.length=null")
                    }
                }
            }, 10000)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun verifyAgainstUserId(param: Map<String?, Any?>?, callback: ActionCallback) {
        callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
        try {
            device!!.verifyAgainstUserId(userID, timeout)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun verifyAll(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
            device!!.verifyAll(timeout)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getId(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.id
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    var fingerprint1: Fingerprint? = null
    fun getFingerprint(param: Map<String?, Any?>?, callback: ActionCallback) {
        callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
        try {
            fingerprint1 = device!!.getFingerprint(ISOFINGERPRINT_TYPE_DEFAULT)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun verifyAgainstFingerprint(param: Map<String?, Any?>?, callback: ActionCallback) {
        callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
        try {
            device!!.verifyAgainstFingerprint(fingerprint1, timeout)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun storeFeature(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.storeFeature(userID, fingerprint1)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun match(param: Map<String?, Any?>?, callback: ActionCallback) {
        callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
        try {
            val fingerprint2 = device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005)
            device!!.match(fingerprint1, fingerprint2)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun compare(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            if (fingerprint1 == null) {
                sendFailedLog(mContext!!.getString(R.string.call_get_fingerprint))
            } else {
                callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
                val fingerprint2 = device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005)
                val result = device!!.compare(fingerprint1!!.feature, fingerprint2.feature)
                if (result == 0) {
                    sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + result)
                } else {
                    sendFailedLog(mContext!!.getString(R.string.operation_failed) + result)
                }
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun identify(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            val fpList: MutableList<*> = ArrayList<Any?>()
            callback.sendResponse(mContext!!.getString(R.string.press_fingerprint) + "1")
            fpList.add(device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005).feature as Nothing)
            callback.sendResponse(mContext!!.getString(R.string.press_fingerprint) + "2")
            fpList.add(device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005).feature as Nothing)
            callback.sendResponse(mContext!!.getString(R.string.press_fingerprint) + "3")
            fpList.add(device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005).feature as Nothing)
            callback.sendResponse(mContext!!.getString(R.string.press_fingerprint) + "4")
            fpList.add(device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005).feature as Nothing)
            callback.sendResponse(mContext!!.getString(R.string.waiting))
            val matchResultIndex = device!!.identify(fingerprint1!!.feature, fpList, 3)
            Log.d("matchResultIndex", "matchResultIndex = " + matchResultIndex.size)
            if (matchResultIndex.size > 0) {
                for (index in matchResultIndex) {
                    sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " , No." + index)
                }
            } else {
                sendFailedLog(mContext!!.getString(R.string.not_get_match))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun formatFingerConvert(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            if (fingerprint1 == null) {
                sendFailedLog(mContext!!.getString(R.string.call_get_fingerprint))
            } else {
                callback.sendResponse(mContext!!.getString(R.string.press_fingerprint))
                val fingerprint2 = device!!.getFingerprint(ISOFINGERPRINT_TYPE_ISO2005)
                val ss = device!!.convertFormat(fingerprint2.feature, 0, 1)
                val result = device!!.compare(fingerprint1!!.feature, fingerprint2.feature as Nothing)
                if (result == 0) {
                    sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + result)
                } else {
                    sendFailedLog(mContext!!.getString(R.string.operation_failed) + result)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun convertFormat(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val fingerprint2 = Fingerprint()
            val arryFea = ByteArray(8192)
            fingerprint2.feature = arryFea
            device!!.convertFormat(fingerprint1, ISOFINGERPRINT_TYPE_ISO2005, fingerprint2, ISOFINGERPRINT_TYPE_DEFAULT)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listAllFingersStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.listAllFingersStatus()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun delFinger(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.delFinger(userID)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun delAllFingers(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.delAllFingers()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun close(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.close()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    companion object {
        private const val ISOFINGERPRINT_TYPE_DEFAULT = 0
        private var ISOFINGERPRINT_TYPE_ISO2005 = 1
        private const val ISOFINGERPRINT_TYPE_ISO2015 = 2
    }
}