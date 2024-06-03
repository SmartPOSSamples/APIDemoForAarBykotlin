package com.cloudpos.androidmvcmodel.callback

import android.content.Context
import android.os.Handler
import android.os.Message
import android.widget.TextView
import com.cloudpos.androidmvcmodel.helper.LogHelper

class HandlerCallback
/**
 * Output information to the display
 */(private val context: Context, private val txtResult: TextView) : Handler.Callback {
    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            LOG -> LogHelper.infoAppendMsg(msg.obj as String, txtResult)
            LOG_SUCCESS -> LogHelper.infoAppendMsgForSuccess(msg.obj as String, txtResult)
            LOG_FAILED -> LogHelper.infoAppendMsgForFailed(msg.obj as String, txtResult)
            ALERT_SOUND -> showDialog(msg.obj.toString())
            else -> LogHelper.infoAppendMsg(msg.obj as String, txtResult)
        }
        return true
    }

    private fun showDialog(testItem: String) {
        val showMsgAndItems = testItem.split("/").toTypedArray()
        //        new AlertDialog.Builder(context)
//                .setTitle(showMsgAndItems[0])
//                .setPositiveButton(context.getString(R.string.sound_btn_success),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                setSuccessfullResult(showMsgAndItems[1], showMsgAndItems[2]);
//                            }
//                        })
//                .setNegativeButton(context.getString(R.string.sound_btn_failed),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                setFailedResult(showMsgAndItems[1], showMsgAndItems[2]);
//                            }
//                        }).show();
    }

    private fun setSuccessfullResult(mainItem: String, subItem: String) {
//        DBHelper.getInstance().saveTestResult(mainItem, subItem, SqlConstants.RESULT_SUCCESS);
    }

    private fun setFailedResult(mainItem: String, subItem: String) {
//        DBHelper.getInstance().saveTestResult(mainItem, subItem, SqlConstants.RESULT_FAILED);
    }

    companion object {
        const val LOG = 1
        const val LOG_SUCCESS = 2
        const val LOG_FAILED = 3
        const val ALERT_SOUND = 4
    }
}