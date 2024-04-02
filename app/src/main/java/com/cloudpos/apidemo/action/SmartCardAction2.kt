package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemo.util.StringUtility
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.card.CPUCard
import com.cloudpos.card.Card
import com.cloudpos.card.SLE4442Card
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.smartcardreader.SmartCardReaderDevice
import com.cloudpos.smartcardreader.SmartCardReaderOperationResult

/*
*   PSAM Card
* */
class SmartCardAction2 : ActionModel() {
    private var device: SmartCardReaderDevice? = null
    private var psamCard: Card? = null
    var area = SLE4442Card.MEMORY_CARD_AREA_MAIN
    var address = 0
    var length = 10
    var logicalID = SmartCardReaderDevice.ID_PSAMCARD
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
                    psamCard = (arg0 as SmartCardReaderOperationResult).card
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
                psamCard = (operationResult as SmartCardReaderOperationResult).card
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

    fun getID(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val cardID = psamCard!!.id
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Card ID = "
                    + StringUtility.byteArray2String(cardID))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getProtocol(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val protocol = psamCard!!.protocol
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Protocol = "
                    + protocol)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getCardStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val cardStatus = psamCard!!.cardStatus
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Card Status = "
                    + cardStatus)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun connect(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val atr = (psamCard as CPUCard?)!!.connect()
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
        val arryAPDU1 = byteArrayOf(
                0x00, 0x84.toByte(), 0x00, 0x00, 0x08
        )
        try {
            val apduResponse = (psamCard as CPUCard?)!!.transmit(arryAPDU)
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
            (psamCard as CPUCard?)!!.disconnect()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun close(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            psamCard = null
            device!!.close()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }
}