package com.askjeffreyliu.floydsteinbergdithering

import android.graphics.*
import com.cloudpos.mvc.common.Logger

class Utils {
    fun floydSteinbergDithering(originalColorBitmap: Bitmap): Bitmap {
        val bmpGrayScaled = toGrayscale(originalColorBitmap)
        floydSteinbergNative(bmpGrayScaled)
        return bmpGrayScaled
    }

    fun binaryBlackAndWhite(originalColorBitmap: Bitmap): Bitmap {
        val bmpGrayScaled = toGrayscale(originalColorBitmap)
        binaryBlackAndWhiteNative(bmpGrayScaled)
        return bmpGrayScaled
    }

    fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
        val bmpGrayscale = Bitmap.createBitmap(bmpOriginal.width, bmpOriginal.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0.0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint)
        return bmpGrayscale
    }

    companion object {
        private external fun floydSteinbergNative(var1: Bitmap)
        private external fun binaryBlackAndWhiteNative(var1: Bitmap)
    }

    init {
        Logger.debug("loadLibrary+++")
        System.loadLibrary("fsdither")
        Logger.debug("loadLibrary---")
    }
}