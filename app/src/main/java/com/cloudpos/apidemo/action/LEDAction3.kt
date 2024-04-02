package com.cloudpos.apidemo.action

import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.led.LEDDevice
import com.cloudpos.mvc.base.ActionCallback

class LEDAction3 : ActionModel() {
    //    private LEDDevice device = new LEDDeviceImpl();
    private var device: LEDDevice? = null
    private val logicalID = LEDDevice.ID_GREEN
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.led", logicalID) as LEDDevice
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.open(logicalID)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getLogicalID(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val logicalID = device!!.logicalID
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Logical ID = "
                    + logicalID)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun startBlink(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.startBlink(100, 100, 10)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
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

    fun cancelBlink(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.cancelBlink()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun blink(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            device!!.blink(100, 100, 100)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val status = device!!.status
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Status: "
                    + if (status == LEDDevice.STATUS_ON) "ON" else "OFF")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun turnOn(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.turnOn()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun turnOff(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.turnOff()
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