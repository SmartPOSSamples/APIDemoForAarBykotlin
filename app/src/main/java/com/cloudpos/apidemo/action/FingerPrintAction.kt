package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.fingerprint.Fingerprint
import com.cloudpos.fingerprint.FingerprintDevice
import com.cloudpos.mvc.base.ActionCallback

class FingerPrintAction : ActionModel() {
    private var device: FingerprintDevice? = null
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext).getDevice("cloudpos.device.fingerprint") as FingerprintDevice
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.open(FingerprintDevice.FINGERPRINT)
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

    fun match(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendNormalLog(mContext!!.getString(R.string.scan_fingerprint_first))
            val fingerprint1 = fingerprint
            sendNormalLog(mContext!!.getString(R.string.scan_fingerprint_second))
            val fingerprint2 = fingerprint
            if (fingerprint1 != null && fingerprint2 != null) {
                val match = device!!.match(fingerprint1, fingerprint2)
                sendSuccessLog(mContext!!.getString(R.string.match_fingerprint_result) + match)
            } else {
                sendFailedLog(mContext!!.getString(R.string.match_fingerprint_fail))
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

    fun close(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.close()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    private val fingerprint: Fingerprint?
        private get() {
            var fingerprint: Fingerprint? = null
            try {
                val operationResult = device!!.waitForFingerprint(TimeConstants.FOREVER)
                if (operationResult.resultCode == OperationResult.SUCCESS) {
                    fingerprint = operationResult.getFingerprint(0, 0)
                    sendSuccessLog2(mContext!!.getString(R.string.scan_fingerprint_success))
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.scan_fingerprint_fail))
                }
            } catch (e: DeviceException) {
                e.printStackTrace()
                sendFailedLog2(mContext!!.getString(R.string.operation_failed))
            }
            return fingerprint
        }
}