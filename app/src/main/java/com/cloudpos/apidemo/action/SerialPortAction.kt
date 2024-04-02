package com.cloudpos.apidemo.action

import com.cloudpos.DeviceException
import com.cloudpos.OperationListener
import com.cloudpos.OperationResult
import com.cloudpos.POSTerminal
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.mvc.common.Logger
import com.cloudpos.mvc.impl.ActionCallbackImpl
import com.cloudpos.sdk.common.SystemProperties
import com.cloudpos.serialport.SerialPortDevice
import com.cloudpos.serialport.SerialPortOperationResult

/**
 * create by rf.w 19-8-7下午3:23
 */
class SerialPortAction : ActionModel() {
    private var device: SerialPortDevice? = null
    private val timeout = 5000
    private val baudrate = 38400
    private val testString = "cloudpos"
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext).getDevice("cloudpos.device.serialport") as SerialPortDevice
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            /**
             * int ID_USB_SLAVE_SERIAL = 0;
             * int ID_USB_HOST_SERIAL = 1;
             * int ID_SERIAL_EXT = 2;
             */
            device!!.open(SerialPortDevice.ID_SERIAL_EXT)
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

    fun waitForRead(param: Map<String?, Any?>?, callback: ActionCallbackImpl?) {
        try {
            val arryData = ByteArray(testString.length)
            val serialPortOperationResult = device!!.waitForRead(arryData.size, timeout)
            if (serialPortOperationResult.data != null) {
                sendSuccessLog("Result = " + String(serialPortOperationResult.data))
                sendSuccessLog(mContext!!.getString(R.string.port_waitforread_succeed))
            } else {
                sendFailedLog(mContext!!.getString(R.string.port_waitforread_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listenForRead(param: Map<String?, Any?>?, callback: ActionCallbackImpl?) {
        val arryData = ByteArray(testString.length)
        try {
            val listener = OperationListener { arg0 ->
                Logger.debug("arg0 getResultCode = " + arg0.resultCode + "")
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    val data = (arg0 as SerialPortOperationResult).data
                    sendSuccessLog2(mContext!!.getString(R.string.port_listenforread_succeed))
                    sendSuccessLog("""
    
    Result = ${String(data)}
    """.trimIndent())
                } else if (arg0.resultCode == OperationResult.ERR_TIMEOUT) {
                    val data = (arg0 as SerialPortOperationResult).data
                    sendSuccessLog2(mContext!!.getString(R.string.port_listenforread_succeed))
                    sendSuccessLog("""
    
    Result = ${String(data)}
    """.trimIndent())
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.port_listenforread_failed))
                }
            }
            device!!.write(arryData, 0, arryData.size)
            device!!.listenForRead(arryData.size, listener, timeout)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun write(param: Map<String?, Any?>?, callback: ActionCallbackImpl?) {
        try {
            val arryData = testString.toByteArray()
            val length = 5
            val offset = 2
            device!!.write(arryData, 0, length)
            sendSuccessLog2(mContext!!.getString(R.string.port_write_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    private enum class Mode {
        SLAVE, HOST
    }

    private fun getModelName(mode: Mode): String {
        //    	"USB_SLAVE_SERIAL" : slave mode,(USB)
//    	"USB_HOST_SERIAL" : host mode(OTG)
        var deviceName: String
        val model = SystemProperties.getSystemPropertie("ro.wp.product.model").trim { it <= ' ' }.replace(" ", "_")
        if (mode == Mode.SLAVE) {
            deviceName = "USB_SLAVE_SERIAL"
            if (model.equals("W1", ignoreCase = true) || model.equals("W1V2", ignoreCase = true)) {
                deviceName = "DB9"
            } else if (model.equals("Q1", ignoreCase = true)) {
                deviceName = "WIZARHANDQ1"
            }
        } else {
            deviceName = "USB_SERIAL"
            if (model.equals("W1", ignoreCase = true) || model.equals("W1V2", ignoreCase = true)) {
                deviceName = "GS0_Q1"
            }
        }
        return deviceName
    }
}