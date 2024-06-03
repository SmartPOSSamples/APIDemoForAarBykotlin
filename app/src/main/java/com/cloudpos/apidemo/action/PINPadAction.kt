package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemo.common.Common
import com.cloudpos.apidemo.util.StringUtility
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.pinpad.KeyInfo
import com.cloudpos.pinpad.PINPadDevice
import com.cloudpos.pinpad.PINPadOperationResult
import com.cloudpos.pinpad.extend.PINPadExtendDevice
import java.nio.charset.StandardCharsets

class PINPadAction : ActionModel() {
    private var device: PINPadExtendDevice? = null
    private val masterKeyID = 0
    private val userKeyID = 0
    private val algo = 0
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.pinpad") as PINPadExtendDevice
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

    fun showText(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.showText(0, "Password balance yuan")
            device!!.showText(1, "show test")
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun clearText(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.clearText()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun setPINLength(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.setPINLength(4, 12)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getSN(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val sn = device!!.sn
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " Pinpad SN = " + sn)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getRandom(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val random = device!!.getRandom(5)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " random = "
                    + StringUtility.byteArray2String(random))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun updateUserKey1(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val userKey = "09 FA 17 0B 03 11 22 76 09 FA 17 0B 03 11 22 76"
        //        String userKey = "CF 5F B3 6E 81 DD A3 C5 B2 D9 F7 3B D9 FF C0 48";
        val arryCipherNewUserKey = ByteArray(16)
        StringUtility.StringToByteArray(userKey, arryCipherNewUserKey)
        try {
            device!!.updateUserKey(masterKeyID, userKeyID, arryCipherNewUserKey)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun updateUserKey2(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val userKey = "09 FA 17 0B 03 11 22 76 09 FA 17 0B 03 11 22 76" //密文
        val arryCipherNewUserKey = ByteArray(16)
        StringUtility.StringToByteArray(userKey, arryCipherNewUserKey)
        val checkValue = "A5 17 3A D5"
        val arryCheckValue = ByteArray(4)
        StringUtility.StringToByteArray(checkValue, arryCheckValue)
        try {
            device!!.updateUserKey(masterKeyID, userKeyID, arryCipherNewUserKey,
                    PINPadDevice.CHECK_TYPE_CUP, arryCheckValue)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun updateUserKey3(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val userKey = "09 FA 17 0B 03 11 22 76 09 FA 17 0B 03 11 22 76"
        val arryCipherNewUserKey = ByteArray(16)
        StringUtility.StringToByteArray(userKey, arryCipherNewUserKey)
        val checkValue = "A5 17 3A"
        val arryCheckValue = ByteArray(3)
        StringUtility.StringToByteArray(checkValue, arryCheckValue)
        try {
            device!!.updateUserKey(masterKeyID, userKeyID, arryCipherNewUserKey,
                    PINPadDevice.CHECK_TYPE_CUP, arryCheckValue)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getSessionKeyCheckValue(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val getSessionKeyCheckValue = device!!.getSessionKeyCheckValue(masterKeyID, userKeyID, algo)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " getSessionKeyCheckValue = "
                    + StringUtility.byteArray2String(getSessionKeyCheckValue))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun encryptData(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 2,
                AlgorithmConstants.ALG_3DES)
        val plain = byteArrayOf(
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38,
                0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38, 0x38)
        val plain2 = ByteArray(888)
        val asda = "57 13 45 57 88 05 64 41 63 31 D2 40 52 26 20 68 10 11 00 00 0F 5F 20 0C 50 41 59 57 41 56 45 2F 56 49 53 41 5F 2A 02 01 56 5F 34 01 01 82 02 20 00 84 07 A0 00 00 00 03 10 10 95 05 00 00 00 00 00 9A 03 21 05 31 9B 02 00 00 9C 01 00 9F 01 06 00 00 00 12 34 56 9F 02 06 00 00 00 06 66 66 9F 09 02 00 96 9F 10 07 06 01 0A 03 A0 28 00 9F 15 02 33 33 9F 16 0F 31 32 33 34 35 36 37 38 20 20 20 20 20 20 20 9F 1A 02 01 56 9F 1C 08 30 30 30 30 30 30 32 35 9F 21 03 01 40 02 9F 26 08 46 29 50 27 0A 58 F8 6B 9F 27 01 80 9F 33 03 E0 F0 C8 9F 34 03 00 00 00 9F 35 01 22 9F 36 02 02 7C 9F 37 04 D2 5C 2C 9B 9F 39 01 07 9F 41 04 00 00 00 42 9F 66 04 36 20 C0 00 9F 6C 02 28 40 00 00 00"
        val i = StringUtility.StringToByteArray(asda, plain2)
        val plain3 = ByteArray(i)
        System.arraycopy(plain2, 0, plain3, 0, 0)
        try {
            val cipher = device!!.encryptData(keyInfo, plain3, 1, ByteArray(8), 8)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " cipher data = "
                    + StringUtility.byteArray2String(cipher))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun calculateMAC(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 1,
                AlgorithmConstants.ALG_3DES)
        val arryMACInData = Common.createMasterKey(8)
        try {
            val mac = device!!.calculateMac(keyInfo, AlgorithmConstants.ALG_MAC_METHOD_X99,
                    arryMACInData)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " mac data = "
                    + StringUtility.byteArray2String(mac))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    private val dukptMAC = byteArrayOf(0x34, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x39, 0x44, 0x39, 0x38, 0x37, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
    fun calculateDukptMAC(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT_2009, 0, 0,
                AlgorithmConstants.ALG_3DES)
        try {
            val mac = device!!.calculateMac(keyInfo, AlgorithmConstants.ALG_MAC_METHOD_SE919, dukptMAC)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " mac data = " + StringUtility.byteArray2String(mac))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun verifyResponseMAC(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_TDUKPT_2009, masterKeyID, 0,
                AlgorithmConstants.ALG_3DES)
        try {
            val mac = device!!.calculateMac(keyInfo, AlgorithmConstants.ALG_MAC_METHOD_SE919, dukptMAC)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " mac data = " + StringUtility.byteArray2String(mac))
            val verifyRespMACArr = ByteArray(8)
            StringUtility.StringToByteArray("20 36 42 23 C1 FF 00 FA", verifyRespMACArr) //0001次的response mac
            val macFlag = 2
            val nDirection = 0
            val b = device!!.verifyResponseMac(keyInfo, dukptMAC, macFlag, verifyRespMACArr, nDirection)
            if (b) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + " request mac = " + StringUtility.byteArray2String(verifyRespMACArr))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun listenForPinBlock(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0, AlgorithmConstants.ALG_3DES)
        val pan = "5399834492433446"
        try {
            val listener = OperationListener { arg0 ->
                sendFailedLog2(arg0.resultCode.toString() + "")
                if (arg0.resultCode == OperationResult.SUCCESS) {
                    val pinBlock = (arg0 as PINPadOperationResult).encryptedPINBlock
                    sendSuccessLog2("PINBlock = " + StringUtility.byteArray2String(pinBlock))
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.operation_failed))
                }
            }
            device!!.listenForPinBlock(keyInfo, pan, false, listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun waitForPinBlock(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0, 4)
        val pan = "0123456789012345678"
        try {
            sendSuccessLog("")
            val operationResult: OperationResult = device!!.waitForPinBlock(keyInfo, pan, false,
                    TimeConstants.FOREVER)
            if (operationResult.resultCode == OperationResult.SUCCESS) {
                val pinBlock = (operationResult as PINPadOperationResult).encryptedPINBlock
                sendSuccessLog2("PINBlock = " + StringUtility.byteArray2String(pinBlock))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun setGUIConfiguration(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val flag = 1
            val data = ByteArray(1)
            data[0] = 0x01
            val data2 = "test1234560".toByteArray()
            val b = device!!.setGUIConfiguration(flag, data)
            if (b) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    var changeSound = 1
    fun setGUISound(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            changeSound++
            val b = device!!.setGUIConfiguration("sound", if (changeSound % 2 == 0) "true" else "false")
            if (b) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + "")
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed) + "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun setGUIStyle(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val b = device!!.setGUIConfiguration("style", "dejavoozcredit")
            if (b) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getMkStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val mkid = 0
            val result = device!!.getMkStatus(mkid)
            if (result == 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + ", it does not exist")
            } else if (result > 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + ",it exist,mkid = " + mkid)
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getSkStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val mkid = 0
            val skid = 0
            val result = device!!.getSkStatus(mkid, skid)
            if (result == 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + ", it does not exist")
            } else if (result > 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + ",it exist,mkid = " + mkid + ",skid = " + skid)
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun getDukptStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val mkid = 0
            val dukptData = ByteArray(32)
            val result = device!!.getDukptStatus(mkid, dukptData)
            if (result == 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + ", it does not exist")
            } else if (result > 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + "getDukptStatus success , KSN = " + StringUtility.ByteArrayToString(dukptData, result))
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun changePin(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0,
                    AlgorithmConstants.ALG_3DES)
            val cardNum = "000000000000000000".toByteArray(StandardCharsets.UTF_8)
            val pinOld = ByteArray(32)
            val pinNew = ByteArray(32)
            val lengthResult = IntArray(2)
            lengthResult[0] = pinOld.size
            lengthResult[1] = pinNew.size
            val timeout = 20000
            device!!.changePin(keyInfo, cardNum, pinOld, pinNew, lengthResult, timeout)
            sendSuccessLog(StringUtility.ByteArrayToString(pinOld, lengthResult[0]))
            sendSuccessLog(StringUtility.ByteArrayToString(pinNew, lengthResult[1]))
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun createPin(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val keyInfo = KeyInfo(PINPadDevice.KEY_TYPE_MK_SK, 0, 0,
                    AlgorithmConstants.ALG_3DES)
            val cardNum = "6210333366668888".toByteArray(StandardCharsets.UTF_8)
            val PinBlock = ByteArray(32)
            val timeout = 20000
            val result = device!!.createPin(keyInfo, cardNum, PinBlock, timeout, 0)
            if (result >= 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + "createPin success , PinBlock = " + StringUtility.ByteArrayToString(PinBlock, result))
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun selectPinblockFormat(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val formatType = 0 //0-5
            val result = device!!.selectPinblockFormat(formatType)
            if (result >= 0) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
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