package com.cloudpos.androidmvcmodel.helper

import android.util.Log

object TerminalHelper {
    const val TERMINAL_TYPE_WIZARPOS_1 = 0
    const val TERMINAL_TYPE_WIZARHAND_Q1 = 1
    const val TERMINAL_TYPE_WIZARHAND_M0 = 2
    const val TERMINAL_TYPE_WIZARPAD_1 = 3
    const val TERMINAL_TYPE_NONE = -1// 找到对应的设备类型WIZARPOS,WIZARPAD,...
    // 判断是否为手持
    /**
     * 获取设备类型<br></br>
     * [.TERMINAL_TYPE_WIZARPOS_1]<br></br>
     * [.TERMINAL_TYPE_WIZARHAND_Q1]<br></br>
     * [.TERMINAL_TYPE_WIZARHAND_M0]<br></br>
     * [.TERMINAL_TYPE_WIZARPAD_1]<br></br>
     */
    var terminalType = TERMINAL_TYPE_NONE
        get() {
            if (field == TERMINAL_TYPE_NONE) {
                field = TERMINAL_TYPE_WIZARPOS_1
                productModel = getProductModel()
                Log.d("model", productModel)
                // 找到对应的设备类型WIZARPOS,WIZARPAD,...
                // 判断是否为手持
                if (productModel == "WIZARHAND_Q1" || productModel == "MSM8610" || productModel == "WIZARHAND_Q0") {
                    field = TERMINAL_TYPE_WIZARHAND_Q1
                } else if (productModel == "FARS72_W_KK" || productModel == "WIZARHAND_M0") {
                    field = TERMINAL_TYPE_WIZARHAND_M0
                } else if (productModel == "WIZARPOS1" || productModel == "WIZARPOS_1") {
                    field = TERMINAL_TYPE_WIZARPOS_1
                } else if (productModel == "WIZARPAD1" || productModel == "WIZARPAD_1") {
                    field = TERMINAL_TYPE_WIZARPAD_1
                }
            }
            return field
        }
        private set
    private var productModel: String? = null

    /**
     * 获取设备的model<br></br>
     * 通过读取 ro.product.model 属性 获得
     */
    fun getProductModel(): String? {
        if (productModel == null) {
            productModel = SystemPropertyHelper.get("ro.product.model").trim { it <= ' ' }
            productModel = productModel!!.replace(" ", "_")
            productModel = productModel!!.toUpperCase()
        }
        return productModel
    }
}