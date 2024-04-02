package com.cloudpos.apidemo.action

import com.cloudpos.*
import com.cloudpos.apidemo.util.StringUtility
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.signature.SignatureDevice
import com.cloudpos.signature.SignatureOperationResult

class SignatureModel : ActionModel() {
    private var device: SignatureDevice? = null
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext).getDevice("cloudpos.device.signature") as SignatureDevice
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

    fun listenSignature(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val listener = OperationListener { operationResult ->
                if (operationResult.resultCode == OperationResult.SUCCESS) {
                    sendSuccessLog2(mContext!!.getString(R.string.sign_succeed))
                    val signatureOperationResult = operationResult as SignatureOperationResult
                    val leg = signatureOperationResult.signatureLength
                    val Data = signatureOperationResult.signatureCompressData
                    val bitmap = signatureOperationResult.signature


//                        String str = StringUtility.ByteArrayToString(Data, Data.length);
//                        String string = String.format("leg = %d , Data = %s",leg, str);
                    sendSuccessLog2(String.format("leg = %d , Data = %s", leg,
                            StringUtility.ByteArrayToString(Data, Data.size)))
                } else {
                    sendFailedLog2(mContext!!.getString(R.string.sign_falied))
                }
            }
            device!!.listenSignature("sign", listener, TimeConstants.FOREVER)
            sendSuccessLog("")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun waitSignature(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            sendSuccessLog("")
            val signatureOperationResult = device!!.waitSignature("sign", TimeConstants.FOREVER)
            if (signatureOperationResult.resultCode == SignatureOperationResult.SUCCESS) {
                sendSuccessLog2(mContext!!.getString(R.string.sign_succeed))
                val leg = signatureOperationResult.signatureLength
                val Data = signatureOperationResult.signatureCompressData
                val bitmap = signatureOperationResult.signature
                sendSuccessLog2(String.format("leg = %d , Data = %s", leg,
                        StringUtility.ByteArrayToString(Data, Data.size)))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.sign_falied))
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