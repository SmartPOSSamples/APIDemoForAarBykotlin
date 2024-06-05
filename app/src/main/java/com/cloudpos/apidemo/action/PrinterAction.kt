package com.cloudpos.apidemo.action

import android.graphics.*
import android.os.Environment
import android.util.Base64
import android.util.Log
import com.askjeffreyliu.floydsteinbergdithering.Utils
import com.cloudpos.*
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.mvc.common.Logger
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice
import com.cloudpos.sdk.printer.impl.PrinterDeviceImpl
import com.cloudpos.serialport.SerialPortDevice
import com.cloudpos.serialport.SerialPortOperationResult
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PrinterAction : ActionModel() {
    private var device: PrinterDevice? = null
    override fun doBefore(param: Map<String?, Any?>?, callback: ActionCallback?) {
        super.doBefore(param, callback)
        if (device == null) {
            device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.printer") as PrinterDevice
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

    fun printText(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.printText(
                    "Demo receipts" +
                            "MERCHANT COPY" +
                            "" +
                            "MERCHANT NAME" +
                            "SHXXXXXXCo.,LTD." +
                            "530310041315039" +
                            "TERMINAL NO" +
                            "50000045" +
                            "OPERATOR" +
                            "50000045" +
                            "" +
                            "CARD NO" +
                            "623020xxxxxx3994 I" +
                            "ISSUER ACQUIRER" +
                            "" +
                            "TRANS TYPE" +
                            "PAY SALE" +
                            "PAY SALE" +
                            "" +
                            "DATE/TIME EXP DATE" +
                            "2005/01/21 16:52:32 2099/12" +
                            "REF NO BATCH NO" +
                            "165232857468 000001" +
                            "VOUCHER AUTH NO" +
                            "000042" +
                            "AMOUT:" +
                            "RMB:0.01" +
                            "" +
                            "BEIZHU" +
                            "SCN:01" +
                            "UMPR NUM:4F682D56" +
                            "TC:EF789E918A548668" +
                            "TUR:008004E000" +
                            "AID:A000000333010101" +
                            "TSI:F800" +
                            "ATC:0440" +
                            "APPLAB:PBOC DEBIT" +
                            "APPNAME:PBOC DEBIT" +
                            "AIP:7C00" +
                            "CUMR:020300" +
                            "IAD:07010103602002010A01000000000005DD79CB" +
                            "TermCap:EOE1C8" +
                            "CARD HOLDER SIGNATURE" +
                            "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES" +
                            "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES" +
                            "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES" +
                            "" +
                            "Demo receipts,do not sign!" +
                            "" +
                            "")
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun printTextForFormat(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_FONT_SIZE, Format.FORMAT_FONT_SIZE_SMALL)
            format.setParameter(Format.FORMAT_FONT_BOLD, Format.FORMAT_FONT_VAL_TRUE)
            device!!.printText(format, """
                             This is printTextForFormat
                             This is printTextForFormat
                             This is printTextForFormat
                             This is printTextForFormat
                             This is printTextForFormat
                             """.trimIndent())
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun sendESCCommand(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val command = byteArrayOf(
                0x1D.toByte(), 0x4C.toByte(), 10, 1 //                (byte) 0x1B, (byte) 0x24, 10,1
        )
        try {
            device!!.sendESCCommand(command)
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun saveBitmap(bitmap: Bitmap) {
        val f = File(Environment.getExternalStorageDirectory().toString() + "/Download/" + System.currentTimeMillis() + ".jpg")
        Log.d("printermodeldemo", "save img," + f.path)
        if (f.exists()) {
            f.delete()
        }
        try {
            val out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Log.d("printermodeldemo", "saved ok")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun queryStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val status = device!!.queryStatus()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed)
                    + " Status: "
                    + if (status == PrinterDevice.STATUS_OUT_OF_PAPER) "out of paper" else "paper exists")
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun cutPaper(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.cutPaper()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }


    fun printBitmap(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            val bitmap = BitmapFactory.decodeStream(mContext?.resources!!.assets
                    .open("printer_barcode_low.png"))
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed) + "\ngetWidth  = " + bitmap.getWidth() + "\ngetHeight = " + bitmap.getHeight());
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        } catch (e: IOException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun printHtml(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val htmlContent1 = "<div><div><center> <font size='4'><b>ទទួលលុយវេរ </b></font></center></div><div><center> <font size='4'><b>............................................................................ </b></font></center></div><font size='4'> លេខកូដដកប្រាក់៖</font><center><div><font size='5'><b> 12941697</b></font></div></center><font size='4'> ទូរស័ព្ទអ្នកទទួល៖</font><center><div><b><font size='4'> 088-888-8888</font></b></div></center><font size='4'> ទឹកប្រាក់៖</font><center><div><b><font size='4'> 5,000 KHR</font></b></div></center><div><center><font size='3'><b>សូមពិនិត្យព័ត៌មាន និងទឹកប្រាក់មុននឹងចាកចេញ</b></font></center></div><div><center><font size='3'>............................................................................</font></center></div><div><font size='3'>ព័ត៌មានសំខាន់៖</font></div><div><font size='3'>- សូមរក្សាវិកយបត្រ មុនពេលប្រាក់ត្រូវបានដក</font></div><div><font size='3'>- លេខកូដដកប្រាក់មានសុពលភាព ៣០ថ្ងៃ</font></div><div><center><font size='3'>............................................................................</font></center></div><div><font size='3'>TID:78767485    2022-02-11 11:51 </font></div><div><center><font size='3'>............................................................................</font></center></div><div><center><font size='2'>សេវាកម្មអតិថិជន 023 220202</font></center></div><div><center><font size='2'>V2.0.0 (POS)</font></center></div></div>"
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
            try {
                device!!.printHTML(mContext, htmlContent1)
                sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
            } catch (e: Exception) {
                sendFailedLog(mContext!!.getString(R.string.operation_failed))
                e.printStackTrace()
            }
//                }
//            });
        } catch (e: Exception) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    fun printBarcode(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val format = Format()
            device!!.printBarcode(format, PrinterDevice.BARCODE_CODE128, "000023123456")
            device!!.printText("\n\n\n")
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

    fun close(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device!!.close()
            sendSuccessLog(mContext!!.getString(R.string.operation_succeed))
        } catch (e: DeviceException) {
            e.printStackTrace()
            sendFailedLog(mContext!!.getString(R.string.operation_failed))
        }
    }

    companion object {
        /*
     *base64 to bitmap
     * import android.util.Base64;
     * import android.graphics.Bitmap;
     * import android.graphics.BitmapFactory;
     */
        fun base64ToBitmap(base64String: String?): Bitmap {
            val bytes = Base64.decode(base64String, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun buf2StringCompact(buf: ByteArray): String {
            var i: Int
            var index: Int
            val sBuf = StringBuilder()
            sBuf.append("[")
            i = 0
            while (i < buf.size) {
                index = if (buf[i] < 0) buf[i] + 256 else buf[i].toInt()
                if (index < 16) {
                    sBuf.append("0").append(Integer.toHexString(index))
                } else {
                    sBuf.append(Integer.toHexString(index))
                }
                sBuf.append(" ")
                i++
            }
            val substring = sBuf.substring(0, sBuf.length - 1)
            return substring + "]".toUpperCase()
        }

        fun isDarkBitamp1(bitmap: Bitmap?): Boolean {
            var isDark = false
            try {
                if (bitmap != null) {
                    var darkPixelCount = 0
                    val x = bitmap.width / 2
                    val y = bitmap.height / 4
                    //取图⽚0-width/2,竖1个像素点height/4
                    for (i in 0 until y) {
                        if (bitmap.isRecycled) {
                            break
                        }
                        val pixelValue = bitmap.getPixel(x, i)
                        //取rgb值颜⾊
                        if (isDark(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue))) {
                            darkPixelCount++
                        }
                    }
                    isDark = darkPixelCount > y / 2
                    Log.i("TAG", "isDartTheme isDark $isDark darkPixelCount = $darkPixelCount")
                }
            } catch (e: Exception) {
                Log.e("TAG", "read wallpaper error")
            }
            return isDark
        }

        //计算像素点亮度算法
        private fun isDark(r: Int, g: Int, b: Int): Boolean {
            return r * 0.299 + g * 0.578 + b * 0.114 < 192
        }

        fun changeBitmapContrastBrightness(bmp: Bitmap, contrast: Float, brightness: Float): Bitmap {
            val cm = ColorMatrix(floatArrayOf(
                    contrast, 0f, 0f, 0f, brightness, 0f, contrast, 0f, 0f, brightness, 0f, 0f, contrast, 0f, brightness, 0f, 0f, 0f, 1f, 0f))
            val retBitmap = Bitmap.createBitmap(bmp.width, bmp.height, bmp.config)
            val canvas = Canvas(retBitmap)
            val paint = Paint()
            paint.colorFilter = ColorMatrixColorFilter(cm)
            canvas.drawBitmap(bmp, 0f, 0f, paint)
            return retBitmap
        }
    }
}