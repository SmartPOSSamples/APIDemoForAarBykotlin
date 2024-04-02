package com.cloudpos.apidemo.action

import android.content.Context
import android.util.Log
import com.android.common.utils.special.Eth0Controller
import com.cloudpos.AlgorithmConstants
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.apidemo.util.ByteConvertStringUtil
import com.cloudpos.apidemo.util.CAController
import com.cloudpos.apidemo.util.MessageUtil
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.hsm.HSMDevice
import com.cloudpos.mvc.base.ActionCallback
import org.bouncycastle.jce.PKCS10CertificationRequest
import org.bouncycastle.openssl.PEMReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.PublicKey
import java.util.*
import javax.security.auth.x500.X500Principal

class HSMModel : ActionModel() {
    private var device: HSMDevice? = null
    private lateinit var CSR_buffer: ByteArray
    var message: String? = null
    var certificate: ByteArray? = null
    private lateinit var encryptBuffer: ByteArray
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.hsm") as HSMDevice
        }
    }

    fun open(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.open()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
    }

    fun isTampered(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val isSuccess = device!!.isTampered
            if (isSuccess == true) {
                sendSuccessLog2(mContext!!.getString(R.string.isTampered_succeed))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.isTampered_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.hsm_falied))
        }
    }

    fun generateKeyPair(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.generateKeyPair(ALIAS_PRIVATE_KEY, AlgorithmConstants.ALG_RSA, 2048)
            sendSuccessLog2(mContext!!.getString(R.string.generateKeyPair_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.generateKeyPair_failed))
        }
    }

    fun injectPublicKeyCertificate(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val bufCert = generatePublicKeyCertificate(mContext)
            val issuccess1 = device!!.injectPublicKeyCertificate(ALIAS_PRIVATE_KEY, ALIAS_PRIVATE_KEY, bufCert, HSMDevice.CERT_FORMAT_PEM)
            if (issuccess1 == true) {
                sendSuccessLog2(mContext!!.getString(R.string.injectPublicKeyCertificate_succeed))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.injectPublicKeyCertificate_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.injectPublicKeyCertificate_failed))
        }
    }

    fun injectRootCertificate(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val `in` = mContext!!.assets.open("testcase_comm_true.crt")
            val length = `in`.available()
            val bufCert = ByteArray(length)
            `in`.read(bufCert)
            Log.e(Eth0Controller.APP_TAG, "注入证书：文件名 :" + "testcase_comm_true.crt" + "  注入别名：" + "testcase_comm_true")
            //            byte[] bufcert = generatePublicKeyCertificate(mContext);
            val issuccess2 = device!!.injectRootCertificate(HSMDevice.CERT_TYPE_COMM_ROOT, ALIAS_COMM_KEY, bufCert, HSMDevice.CERT_FORMAT_PEM)
            if (issuccess2 == true) {
                sendSuccessLog2(mContext!!.getString(R.string.injectRootCertificate_succeed))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.injectRootCertificate_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.injectRootCertificate_failed))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteCertificate(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val isSuccess3 = device!!.deleteCertificate(HSMDevice.CERT_TYPE_PUBLIC_KEY, ALIAS_PRIVATE_KEY)
            val isSuccess4 = device!!.deleteCertificate(HSMDevice.CERT_TYPE_COMM_ROOT, ALIAS_COMM_KEY)
            if (isSuccess3 == true && isSuccess4 == true) {
                sendSuccessLog2(mContext!!.getString(R.string.deleteCertificate_succeed))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.deleteCertificate_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.deleteCertificate_failed))
        }
    }

    fun decrypt(param: Map<String?, Any?>?, callback: ActionCallback?) {
        sendSuccessLog2(mContext!!.getString(R.string.decrypt_data))
        try {
            CSR_buffer = device!!.generateCSR(ALIAS_PRIVATE_KEY, X500Principal("CN=T1,OU=T2,O=T3,C=T4"))
            val buffer = CSR_buffer
            val csr = MessageUtil.readPEMCertificateRequest(CSR_buffer)
            var publicKey: PublicKey? = null
            try {
                publicKey = csr!!.publicKey
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
            }
            val arryEncrypt = MessageUtil.encryptByKey("123456".toByteArray(), publicKey)
            Log.e("TAG", """
     ${arryEncrypt!!.size}
     *------*${ByteConvertStringUtil.bytesToHexString(arryEncrypt)}
     """.trimIndent())
            val plain = device!!.decrypt(AlgorithmConstants.ALG_RSA, ALIAS_PRIVATE_KEY, arryEncrypt)
            //            String string = ByteConvertStringUtil.bytesToHexString(plain);
            val string = String(plain)
            sendSuccessLog2(mContext!!.getString(R.string.decrypt_succeed))
            sendSuccessLog2(string)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.decrypt_failed))
        }
    }

    fun deleteKeyPair(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val issuccess4 = device!!.deleteKeyPair(ALIAS_PRIVATE_KEY)
            if (issuccess4 == true) {
                sendSuccessLog2(mContext!!.getString(R.string.deleteKeyPair_succeed))
            } else {
                sendFailedLog2(mContext!!.getString(R.string.deleteKeyPair_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.deleteKeyPair_failed))
        }
    }

    fun encrypt(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            encryptBuffer = device!!.encrypt(AlgorithmConstants.ALG_RSA, ALIAS_PRIVATE_KEY, "123456".toByteArray())
            val string = ByteConvertStringUtil.bytesToHexString(encryptBuffer)
            sendSuccessLog2("""
    ${mContext!!.getString(R.string.encrypt_succeed)}
    $string
    """.trimIndent())
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.encrypt_failed))
        }
    }

    fun getEncryptedUniqueCode(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val spec = POSTerminal.getInstance(mContext).terminalSpec
        val uniqueCode = spec.uniqueCode
        try {
            val code = device!!.getEncryptedUniqueCode(uniqueCode, "123456")
            sendSuccessLog2(mContext!!.getString(R.string.getEncryptedUniqueCode_succeed))
            sendSuccessLog2(code)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.getEncryptedUniqueCode_failed))
        }
    }

    fun queryCertificates(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val alias = device!!.queryCertificates(HSMDevice.CERT_TYPE_PUBLIC_KEY)
            for (i in alias.indices) {
            }
            sendSuccessLog2("""
    ${mContext!!.getString(R.string.queryCertificates_succeed)}
    ${Arrays.toString(alias)}
    """.trimIndent())
        } catch (e: DeviceException) {
            sendFailedLog2(mContext!!.getString(R.string.queryCertificates_failed))
        }
    }

    fun generateRandom(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val buff = device!!.generateRandom(2)
            //            String string = new String(buff);
//            Log.d("TAG",string);
            sendSuccessLog2(mContext!!.getString(R.string.generateRandom_succeed))
            ByteConvertStringUtil.bytesToHexString(buff)?.let { sendSuccessLog2(it) }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.generateRandom_failed))
        }
    }

    fun getFreeSpace(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val freeSpace = device!!.freeSpace
            sendSuccessLog2(mContext!!.getString(R.string.getFreeSpace_succeed) + freeSpace)
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.getFreeSpace_failed))
        }
    }

    fun generateCSR(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            CSR_buffer = device!!.generateCSR(ALIAS_PRIVATE_KEY, X500Principal("CN=T1,OU=T2,O=T3,C=T4"))
            val string = String(CSR_buffer)
            sendSuccessLog2("""
    ${mContext!!.getString(R.string.generateCSR_succeed)}
    $string
    """.trimIndent())
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.generateCSR_failed))
        }
    }

    fun getCertificate(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            certificate = device!!.getCertificate(HSMDevice.CERT_TYPE_PUBLIC_KEY, ALIAS_PRIVATE_KEY, HSMDevice.CERT_FORMAT_PEM)
            Log.e("TAG", certificate.toString() + "")
            if (certificate != null) {
                val strings = String(certificate!!)
                sendSuccessLog2("""
    ${mContext!!.getString(R.string.getCertificate_succeed)}
    $strings
    """.trimIndent())
            } else {
                sendFailedLog2(mContext!!.getString(R.string.getCertificate_failed))
            }
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog2(mContext!!.getString(R.string.getCertificate_failed))
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

    fun resetStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val bool = device!!.resetSensorStatus()
            if (bool) {
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
            } else {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
            }
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

    fun generatePublicKeyCertificate(mContext: Context?): ByteArray? {
        try {
            CSR_buffer = device!!.generateCSR(ALIAS_PRIVATE_KEY, X500Principal("CN=T1,OU=T2,O=T3,C=T4"))
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
        var publicCerts: ByteArray? = null
        var cSRresult = ByteArray(2048)
        cSRresult = CSR_buffer
        val reader = PEMReader(InputStreamReader(ByteArrayInputStream(cSRresult)))
        try {
            val obj = reader.readObject()
            if (obj is PKCS10CertificationRequest) {
                publicCerts = CAController.getInstance().getPublicCert(mContext!!, obj)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return publicCerts
    } //    public int injectServerCert(String fileName, String alias, Context host, int certType){

    //        int result = -1;
    //        try {
    //            InputStream in = host.getAssets().open(fileName+".crt");
    //            int length = in.available();
    //            byte[] bufCert = new byte[length];
    //            in.read(bufCert);
    //            Log.e(APP_TAG, "inject cert：fileName :"+ fileName + "  alias：" + alias);
    //            result = injectServerCert(alias, bufCert,certType);
    //            Log.e(APP_TAG, "result = " + result);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return result;
    //    }
    companion object {
        const val ALIAS_PRIVATE_KEY = "hsm_pri"
        const val ALIAS_COMM_KEY = "testcase_comm_true"
    }
}