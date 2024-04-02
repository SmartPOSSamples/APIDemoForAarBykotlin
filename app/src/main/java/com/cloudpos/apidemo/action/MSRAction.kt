package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemo.util.StringUtility
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.msr.MSRDevice
import com.cloudpos.msr.MSROperationResult
import com.cloudpos.msr.MSRTrackData
import com.cloudpos.mvc.base.ActionCallback

class MSRAction : ActionModel() {
    //    private MSRDevice device = new MSRDeviceImpl();
    private var device: MSRDevice? = null
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.msr") as MSRDevice
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.open()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listenForSwipe(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val listener = OperationListener { arg0 ->
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    sendSuccessLog2(mContext!!.getString(R.string.find_card_succeed))
                    val data = (arg0 as MSROperationResult).msrTrackData
                    var trackError = 0
                    var trackData: ByteArray? = null
                    for (trackNo in 0..2) {
                        trackError = data.getTrackError(trackNo)
                        if (trackError == MSRTrackData.NO_ERROR) {
                            trackData = data.getTrackData(trackNo)
                            sendSuccessLog2(String.format("trackNO = %d, trackData = %s", trackNo,
                                    StringUtility.ByteArrayToString(trackData, trackData.size)))
                        } else {
                            sendFailedLog2(String.format("trackNO = %d, trackError = %s", trackNo,
                                    trackError))
                        }
                    }
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.find_card_failed))
                }
            }
            device!!.listenForSwipe(listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun waitForSwipe(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            val operationResult: OperationResult = device!!.waitForSwipe(TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.find_card_succeed))
                val data = (operationResult as MSROperationResult).msrTrackData
                var trackError = 0
                var trackData: ByteArray? = null
                for (trackNo in 0..2) {
                    trackError = data.getTrackError(trackNo)
                    if (trackError == MSRTrackData.NO_ERROR) {
                        trackData = data.getTrackData(trackNo)
                        sendSuccessLog2(String.format("trackNO = %d, trackData = %s", trackNo,
                                StringUtility.ByteArrayToString(trackData, trackData.size)))
                    } else {
                        sendFailedLog2(String.format("trackNO = %d, trackError = %s", trackNo,
                                trackError))
                    }
                }
            } else {
                sendFailedLog2(mContext!!.getString(R.string.find_card_failed))
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
}