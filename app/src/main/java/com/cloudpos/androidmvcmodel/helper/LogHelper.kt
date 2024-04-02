/**
 *
 */
package com.cloudpos.androidmvcmodel.helper

import android.graphics.*
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView

/**
 * @author john 打印信息的格式控制类。 红色：出现的问题。 绿色：正常的信息。 黄色：可能出现的问题。 1 : color is black 2
 * : color is yellow 3 : color is blue 4 : color is red other number :
 * color is black;
 */
object LogHelper {
    /**
     * TestView changed color and info message. Called after the TestView is
     * created and whenever the TextView changes. Set your TextView's message
     * here.
     *
     * @param TextView text
     * @param String infoMsg : color is red < color name="red">#FF0000< /color><
     * !--红色 -->
     */
    @Deprecated("")
    fun infoException(text: TextView, infoMsg: String) {
        text.setText(infoMsg, TextView.BufferType.SPANNABLE)
        val start = text.text.length
        val end = start + infoMsg.length
        val style = text.text as Spannable
        style.setSpan(ForegroundColorSpan(Color.RED), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    /**
     * @param TextView text
     * @param String infoMsg : color is black
     */
    @Deprecated("")
    fun info(text: TextView, infoMsg: String?) {
        // int start = text.getText().length();
        // int end = start +infoMsg.length();
        text.text = infoMsg
    }

    /**
     * @param TextView text
     * @param String infoMsg : color is yellow
     */
    @Deprecated("")
    fun infoWarning(text: TextView, infoMsg: String) {
        text.text = infoMsg
        val style = text.text as Spannable
        val start = text.text.length
        val end = start + infoMsg.length
        style.setSpan(ForegroundColorSpan(Color.YELLOW), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    /**
     * @param TextView text
     * @param String infoMsg
     * @param order :set background color. 1 : color is black. 2 : color is
     * yellow. 3 : color is blue .4 : color is red .other number :
     * color is black;
     */
    fun infoMsgAndColor(text: TextView, infoMsg: String, order: Int) {
        text.text = infoMsg
        val style = text.text as Spannable
        val start = 0
        val end = start + infoMsg.length
        val color: ForegroundColorSpan
        color = when (order) {
            1 -> ForegroundColorSpan(Color.BLACK)
            2 -> ForegroundColorSpan(Color.YELLOW)
            3 -> ForegroundColorSpan(Color.BLUE)
            4 -> ForegroundColorSpan(Color.RED)
            else -> ForegroundColorSpan(Color.BLACK)
        }
        style.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun infoAppendMsgAndColor(text: TextView, infoMsg: String, order: Int) {
        var start = 0
        if (text.text.length == 0) {
        } else {
            start = text.text.length
        }
        text.append(infoMsg)
        val style = text.text as Spannable
        val end = start + infoMsg.length
        val color: ForegroundColorSpan
        color = when (order) {
            1 -> ForegroundColorSpan(Color.BLACK)
            2 -> ForegroundColorSpan(Color.YELLOW)
            3 -> ForegroundColorSpan(Color.BLUE)
            4 -> ForegroundColorSpan(Color.RED)
            5 -> ForegroundColorSpan(Color.RED)
            else -> ForegroundColorSpan(Color.YELLOW)
        }
        style.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    /**
     * 蓝色字体打印成功信息
     *
     * @param msg
     * @param text
     */
    fun infoAppendMsgForSuccess(msg: String, text: TextView?) {
        // TextView text = PreMainActivity.txtResult;
        var start = 0
        if (text!!.text.length == 0) {
        } else {
            start = text.text.length
        }
        if (start > 1000) {
            text.text = ""
            start = 0
        }
        text.append(msg)
        val style = text.text as Spannable
        val end = start + msg.length
        val color: ForegroundColorSpan
        color = ForegroundColorSpan(Color.BLUE)
        style.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        moveScroller(text)
        // text.setScrollY(text.getScrollY()+text.getLineHeight());
    }

    /**
     * 红色字体打印失败信息
     *
     * @param msg
     * @param text
     */
    fun infoAppendMsgForFailed(msg: String, text: TextView?) {
        // TextView text = PreMainActivity.txtResult;
        var start = 0
        if (text!!.text.length == 0) {
        } else {
            start = text.text.length
        }
        if (start > 1000) {
            text.text = ""
            start = 0
        }
        text.append(msg)
        val style = text.text as Spannable
        val end = start + msg.length
        val color: ForegroundColorSpan
        color = ForegroundColorSpan(Color.RED)
        style.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        moveScroller(text)
        // Scroller scr = text.
        // text.setScrollY(text.getScrollY()+text.getLineHeight());
    }

    /**
     * 给textresult上用黑色写上命令。
     */
    fun infoAppendMsg(msg: String, text: TextView?) {
        // TextView text = PreMainActivity.txtResult;
        var start = 0
        if (text!!.text.length == 0) {
        } else {
            start = text.text.length
        }
        if (start > 1000) {
            text.text = ""
            start = 0
        }
        text.append(msg)
        val style = text.text as Spannable
        val end = start + msg.length
        val color: ForegroundColorSpan
        color = ForegroundColorSpan(Color.BLACK)
        style.setSpan(color, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        moveScroller(text)
        //
    }

    private fun moveScroller(text: TextView?) {
        // find the amount we need to scroll. This works by
        // asking the TextView's internal layout for the position
        // of the final line and then subtracting the TextView's height
        val scrollAmount = (text!!.layout.getLineTop(text.lineCount)
                - text.height)
        // if there is no need to scroll, scrollAmount will be <=0
        if (scrollAmount > 0) {
            text.scrollTo(0, scrollAmount + 30)
        } else {
            text.scrollTo(0, 0)
        }
    }
}