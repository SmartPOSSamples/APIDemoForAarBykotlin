package com.cloudpos.androidmvcmodel.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cloudpos.androidmvcmodel.MainApplication
import com.cloudpos.androidmvcmodel.entity.MainItem
import com.cloudpos.androidmvcmodel.helper.LanguageHelper
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import java.util.logging.Logger

class ListViewAdapter(private val context: Context) : BaseAdapter() {
    private val inflater: LayoutInflater

    // private DBHelper dbHelper;
    private var mainItemIndex = INDEX_NONE
    override fun getCount(): Int {
        var count = 0
        count = if (mainItemIndex <= INDEX_NONE) {
            MainApplication.Companion.testItems!!.size
        } else {
            MainApplication.Companion.testItems!!.get(mainItemIndex)!!.getSubItems().size
        }
        return count
    }

    override fun getItem(position: Int): Any {
        val item: Any?
        item = if (mainItemIndex <= INDEX_NONE) {
            MainApplication.Companion.testItems.get(position)
        } else {
            MainApplication.Companion.testItems.get(mainItemIndex).getSubItem(position)
        }
        return item
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View?{
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_test, null)
        }
        // TextView txtSignature = (TextView)
        // convertView.findViewById(R.id.txt_signature);
        val txtButton = convertView!!.findViewById<View>(R.id.txt_button) as TextView
        // setDisplayedSignature(position, txtSignature);
        setDisplayedButton(position, txtButton)
        return convertView
    }

    /**
     * refresh listview when switching from MainItem page and SubItem page
     */
    fun refreshView(index: Int) {
        mainItemIndex = index
        Log.e(TAG, "mainItemIndex = $mainItemIndex")
        notifyDataSetChanged()
    }

    /**
     * refresh specified item when changed
     */
    fun refreshChangedItemView(position: Int, convertView: View, parent: ViewGroup) {
        getView(position, convertView, parent)
    }

    // private void setDisplayedSignature(int position, TextView txtSignature) {
    // String testResult = getTestResult(position);
    // Log.e(TAG, "setDisplayedSignature testResult = " + testResult);
    // if (testResult.equals(SqlConstants.RESULT_SUCCESS)) {
    // txtSignature.setText("√");
    // txtSignature.setTextColor(Color.rgb(0, 0, 0));
    // } else if (testResult.equals(SqlConstants.RESULT_FAILED)) {
    // txtSignature.setText("X");
    // txtSignature.setTextColor(Color.rgb(255, 0, 0));
    // } else if (testResult.equals(SqlConstants.RESULT_EXCEPTION)) {
    // txtSignature.setText("O");
    // txtSignature.setTextColor(Color.rgb(255, 255, 255));
    // } else {
    // txtSignature.setText("");
    // txtSignature.setTextColor(Color.rgb(0, 0, 0));
    // }
    // }
    private fun setDisplayedButton(position: Int, txtButton: TextView) {
        val mainItem = getMainItem(position)
        // txtButton.setText(mainItem.getDisplayName(LanguageHelper.getLanguageType(context)));
        if (mainItemIndex <= INDEX_NONE) {
            txtButton.text = mainItem!!.getDisplayName(LanguageHelper.getLanguageType(context))
        } else {
            txtButton.text = mainItem!!.getSubItem(position).getDisplayName(
                    LanguageHelper.getLanguageType(context))
            //            txtButton.setTag(mainItem.getSubItem(position).getDisplayName(LanguageHelper.getLanguageType(context)));
        }
    }

    /**
     * get test result from sqlite database<br></br>
     */
    // private String getTestResult(int position) {
    // MainItem mainItem = getMainItem(position);
    // // String testResult = dbHelper.queryTestResultByMainItem(mainItem);
    // return testResult;
    // }
    private fun getMainItem(position: Int): MainItem? {
        var mainItem: MainItem? = null
        mainItem = if (mainItemIndex <= INDEX_NONE) {
            MainApplication.Companion.testItems!!.get(position)
        } else {
            MainApplication.Companion.testItems!!.get(mainItemIndex)
        }
        return mainItem
    }

    companion object {
        private const val TAG = "ListViewAdapter"
        const val INDEX_NONE = -1
    }

    init {
        inflater = LayoutInflater.from(context)
        // dbHelper = DBHelper.getInstance();
    }
}