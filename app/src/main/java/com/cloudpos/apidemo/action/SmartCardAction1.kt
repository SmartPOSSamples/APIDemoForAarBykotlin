package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemo.common.Common
import com.cloudpos.apidemo.util.StringUtility
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.card.CPUCard
import com.cloudpos.card.Card
import com.cloudpos.card.SLE4442Card
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.smartcardreader.SmartCardReaderDevice
import com.cloudpos.smartcardreader.SmartCardReaderOperationResult

/*
*   IC Card
* */
class SmartCardAction1 : ActionModel() {
    private var device: SmartCardReaderDevice? = null
    private var icCard: Card? = null
    var area = SLE4442Card.MEMORY_CARD_AREA_MAIN
    var address = 0
    var length = 10
    var logicalID = SmartCardReaderDevice.ID_SMARTCARD
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.smartcardreader", logicalID) as SmartCardReaderDevice
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

    fun listenForCardPresent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val listener = OperationListener { arg0 ->
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    sendSuccessLog2(mContext!!.getString(R.string.find_card_succeed))
                    icCard = (arg0 as SmartCardReaderOperationResult).card
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.find_card_failed))
                }
            }
            device!!.listenForCardPresent(listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun waitForCardPresent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            val operationResult: OperationResult = device!!.waitForCardPresent(TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.find_card_succeed))
                icCard = (operationResult as SmartCardReaderOperationResult).card
            } else {
                sendFailedLog2(mContext!!.getString(R.string.find_card_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listenForCardAbsent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val listener = OperationListener { arg0 ->
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    sendSuccessLog2(mContext!!.getString(R.string.absent_card_succeed))
                    icCard = null
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.absent_card_failed))
                }
            }
            device!!.listenForCardAbsent(listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun waitForCardAbsent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            val operationResult: OperationResult = device!!.waitForCardAbsent(TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.absent_card_succeed))
                icCard = null
            } else {
                sendFailedLog2(mContext!!.getString(R.string.absent_card_failed))
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

    fun getID(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val cardID = icCard!!.id
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Card ID = "
                    + StringUtility.byteArray2String(cardID))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getProtocol(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val protocol = icCard!!.protocol
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Protocol = "
                    + protocol)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getCardStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val cardStatus = icCard!!.cardStatus
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Card Status = "
                    + cardStatus)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun verify(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val key = byteArrayOf(
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte()
        )
        try {
            val verifyResult = (icCard as SLE4442Card?)!!.verify(key)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun read(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val result = (icCard as SLE4442Card?)!!.read(area, address, length)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " (" + area
                    + ", " + address + ") memory data: " + StringUtility.byteArray2String(result))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun write(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val arryData = Common.createMasterKey(10)
        try {
            (icCard as SLE4442Card?)!!.write(area, address, arryData)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun connect(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val atr = (icCard as CPUCard?)!!.connect()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " ATR: "
                    + StringUtility.byteArray2String(atr.bytes))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun transmit(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val arryAPDU = byteArrayOf(
                0x00, 0x84.toByte(), 0x00, 0x00, 0x08
        )
        try {
            val apduResponse = (icCard as CPUCard?)!!.transmit(arryAPDU)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " APDUResponse: "
                    + StringUtility.byteArray2String(apduResponse))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun disconnect(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendNormalLog(mContext!!.getString(R.string.rfcard_remove_card))
            (icCard as CPUCard?)!!.disconnect()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun close(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            icCard = null
            device!!.close()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }
}