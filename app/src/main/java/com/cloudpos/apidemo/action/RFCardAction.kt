package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemo.common.Common
import com.cloudpos.apidemo.util.StringUtility
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.card.*
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.mvc.impl.ActionCallbackImpl
import com.cloudpos.rfcardreader.RFCardReaderDevice
import com.cloudpos.rfcardreader.RFCardReaderOperationResult

class RFCardAction : ActionModel() {
    private var device: RFCardReaderDevice? = null
    var rfCard: Card? = null

    /* The index needs to be adjusted according to the actual situation.
     * */
    // mifare card : 2-63,012;
    //ultralight card : 0,4-63
    var sectorIndex = 0
    var blockIndex = 1
    private val pinType_level3 = 2
    var cardType = -1
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.rfcardreader") as RFCardReaderDevice
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.open()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun listenForCardPresent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val listener = OperationListener { arg0 ->
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    rfCard = (arg0 as RFCardReaderOperationResult).card
                    try {
                        val cardTypeValue = device!!.cardTypeValue
                        cardType = cardTypeValue[0]
                        if (cardType == MIFARE_CARD_S50 || cardType == MIFARE_CARD_S70) {
                            sectorIndex = 3
                            blockIndex = 0 //0,1,2
                        } else if (cardType == MIFARE_ULTRALIGHT_CARD) {
                            sectorIndex = 0
                            blockIndex = 5 //4-63
                        }
                        sendSuccessLog2(mContext!!.getString(R.string.find_card_succeed) + ",cardType=" + cardType + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
                    } catch (e: DeviceException) {
                        e.printStackTrace()
                    }
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.find_card_failed))
                }
            }
            device!!.listenForCardPresent(listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun waitForCardPresent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            val operationResult: OperationResult = device!!.waitForCardPresent(TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.find_card_succeed))
                rfCard = (operationResult as RFCardReaderOperationResult).card
            } else {
                sendFailedLog2(mContext!!.getString(R.string.find_card_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun listenForCardAbsent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val listener = OperationListener { arg0 ->
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    sendSuccessLog2(mContext!!.getString(R.string.absent_card_succeed))
                    rfCard = null
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.absent_card_failed))
                }
            }
            device!!.listenForCardAbsent(listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun waitForCardAbsent(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            val operationResult: OperationResult = device!!.waitForCardAbsent(TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.absent_card_succeed))
                rfCard = null
            } else {
                sendFailedLog2(mContext!!.getString(R.string.absent_card_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun cancelRequest(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.cancelRequest()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun getMode(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val mode = device!!.mode
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Mode = " + mode)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun setSpeed(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.speed = 460800
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun getSpeed(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val speed = device!!.speed
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Speed = " + speed)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun getID(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val cardID = rfCard!!.id
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Card ID = "
                    + StringUtility.byteArray2String(cardID))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun getProtocol(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val protocol = rfCard!!.protocol
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Protocol = "
                    + protocol)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun getCardStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val cardStatus = rfCard!!.cardStatus
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Card Status = "
                    + cardStatus)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun verifyKeyA(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val key = byteArrayOf(
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte()
        )
        try {
            val verifyResult = (rfCard as MifareCard?)!!.verifyKeyA(sectorIndex, key)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun verifyKeyB(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val key = byteArrayOf(
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0xFF.toByte()
        )
        try {
            val verifyResult = (rfCard as MifareCard?)!!.verifyKeyB(sectorIndex, key)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun verify_level3(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val arryKey = byteArrayOf(
                0x49.toByte(), 0x45.toByte(), 0x4D.toByte(), 0x4B.toByte(), 0x41.toByte(), 0x45.toByte(), 0x52.toByte(), 0x42.toByte(),
                0x21.toByte(), 0x4E.toByte(), 0x41.toByte(), 0x43.toByte(), 0x55.toByte(), 0x4F.toByte(), 0x59.toByte(), 0x46.toByte()
        )
        try {
            val verifyLevel3Result = (rfCard as MifareUltralightCard?)!!.verifyKey(arryKey)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun transmit_level3(param: Map<String?, Any?>?, callback: ActionCallbackImpl?) {
        val arryAPDU = byteArrayOf(
                0x30.toByte(), 0x00.toByte()
        )
        try {
            var result: ByteArray? = null
            if (rfCard is CPUCard) {
                result = (rfCard as CPUCard).transmit(arryAPDU, 0)
            } else if (rfCard is MifareCard) {
                result = (rfCard as MifareCard).transmit(arryAPDU, 0)
            } else if (rfCard is MifareUltralightCard) {
                result = (rfCard as MifareUltralightCard).transmit(arryAPDU, 0)
            } else {
                //result = (Real Card Type) rfCard.transmit(arryAPDU, 0);
            }
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " (" + sectorIndex
                    + ", " + blockIndex + ")transmit_level3: " + StringUtility.byteArray2String(result))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun sendControlCommand(param: Map<String?, Any?>?, callback: ActionCallbackImpl?) {
        val cmdID = 0x80
        val command = byteArrayOf(
                0x01.toByte()
        )
        try {
            val result = device!!.sendControlCommand(cmdID, command)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + result)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun readBlock(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val result = (rfCard as MifareCard?)!!.readBlock(sectorIndex, blockIndex)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " (" + sectorIndex
                    + ", " + blockIndex + ")Block data: " + StringUtility.byteArray2String(result))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun writeBlock(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val arryData = Common.createMasterKey(16) // 随机创造16个字节的数组
        try {
            (rfCard as MifareCard?)!!.writeBlock(sectorIndex, blockIndex, arryData)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun readValue(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val value = (rfCard as MifareCard?)!!.readValue(sectorIndex, blockIndex)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " value = "
                    + value.money + " user data: "
                    + StringUtility.byteArray2String(value.userData))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun writeValue(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val value = MoneyValue(byteArrayOf(
                    0x39.toByte()
            ), 1024)
            (rfCard as MifareCard?)!!.writeValue(sectorIndex, blockIndex, value)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun incrementValue(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            (rfCard as MifareCard?)!!.increaseValue(sectorIndex, blockIndex, 10)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun decrementValue(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            (rfCard as MifareCard?)!!.decreaseValue(sectorIndex, blockIndex, 10)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun read(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val result = (rfCard as MifareUltralightCard?)!!.read(blockIndex)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " (" + sectorIndex
                    + ", " + blockIndex + ")Block data: " + StringUtility.byteArray2String(result))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun write(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val arryData = Common.createMasterKey(4) // 随机创造4个字节的数组
        try {
            (rfCard as MifareUltralightCard?)!!.write(blockIndex, arryData)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun connect(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val atr = (rfCard as CPUCard?)!!.connect()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " ATR: "
                    + StringUtility.byteArray2String(atr.bytes))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun transmit(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val arryAPDU = byteArrayOf(
                0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(),
                0x0E.toByte(), 0x32.toByte(), 0x50.toByte(), 0x41.toByte(),
                0x59.toByte(), 0x2E.toByte(), 0x53.toByte(), 0x59.toByte(),
                0x53.toByte(), 0x2E.toByte(), 0x44.toByte(), 0x44.toByte(),
                0x46.toByte(), 0x30.toByte(), 0x31.toByte()
        )
        //byte[] FelicaArryAPDU1 = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0x01, (byte) 0x0B, (byte) 0x00, (byte) 0x01, (byte) 0x80, (byte) 0x04};
        try {
            val apduResponse = (rfCard as CPUCard?)!!.transmit(arryAPDU)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " APDUResponse: "
                    + StringUtility.byteArray2String(apduResponse))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun disconnect(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendNormalLog(mContext!!.getString(R.string.rfcard_remove_card))
            (rfCard as CPUCard?)!!.disconnect()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    fun close(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            rfCard = null
            device!!.close()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed) + ",sectorIndex=" + sectorIndex + ",blockIndex=" + blockIndex)
        }
    }

    companion object {
        /*
*                      0x0000 CONTACTLESS_CARD_TYPE_B_CPU 0x0100
*                      CONTACTLESS_CARD_TYPE_A_CLASSIC_MINI 0x0001
*                      CONTACTLESS_CARD_TYPE_A_CLASSIC_1K 0x0002
*                      CONTACTLESS_CARD_TYPE_A_CLASSIC_4K 0x0003
*                      CONTACTLESS_CARD_TYPE_A_UL_64 0x0004
     */
        private const val CPU_CARD = 0
        private const val MIFARE_CARD_S50 = 6
        private const val MIFARE_CARD_S70 = 3
        private const val MIFARE_ULTRALIGHT_CARD = 4
    }
}