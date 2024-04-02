package com.cloudpos.androidmvcmodel

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.android.common.utils.PackageUtils
import com.cloudpos.androidmvcmodel.adapter.ListViewAdapter
import com.cloudpos.androidmvcmodel.common.Constants
import com.cloudpos.androidmvcmodel.entity.MainItem
import com.cloudpos.androidmvcmodel.helper.LanguageHelper
import com.cloudpos.androidmvcmodel.helper.LogHelper
import com.cloudpos.apidemoforunionpaycloudpossdk.R
import com.cloudpos.mvc.base.ActionCallback
import com.cloudpos.mvc.base.ActionManager
import com.cloudpos.mvc.common.Logger
import com.cloudpos.mvc.impl.ActionCallbackImpl
import java.util.*

class MainActivity : Activity(), OnItemClickListener {
    var txtLog: TextView? = null
//    var txtIntroduction: TextView? = null
    var lvwTestItems: ListView? = null
    var context: Context? = null
    var adapter: ListViewAdapter? = null
    private var isMain = true
    private var clickedPosition = 0
    private var scrollPosition = 0
    private var clickedMainItem: MainItem? = null
    private var handler: Handler? = null
    private var actionCallback: ActionCallback? = null
    private var testParameters: MutableMap<String?, Any?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initParameter()
        initView()
        initUI()
    }

    private fun initParameter() {
        Logger.debug("initParameter +")
        context = this@MainActivity
        adapter = ListViewAdapter(context as MainActivity)
        handler = Handler(handlerCallback)
        actionCallback = ActionCallbackImpl(context, handler!!)
        testParameters = HashMap()
        Logger.debug("initParameter -")
    }

    private fun initView() {
        Logger.debug("initView +")
        txtLog = findViewById<View>(R.id.txt_log) as TextView
//        txtIntroduction = findViewById<View>(R.id.txt_introduction) as TextView
        lvwTestItems = findViewById<View>(R.id.lvw_test_items) as ListView
        Logger.debug("initView -")
    }

    private fun initUI() {
        Logger.debug("initUI +")
        txtLog!!.movementMethod = ScrollingMovementMethod.getInstance()
        lvwTestItems!!.adapter = adapter
        lvwTestItems!!.onItemClickListener = this
        lvwTestItems!!.setOnScrollListener(onTestItemsScrollListener)
        Logger.debug("initUI -")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val cleanLogMenu = menu.addSubMenu(MENU_GROUP_ID, MENU_CLEAN_LOG, Menu.NONE,
                R.string.clean_log)
        cleanLogMenu.setIcon(android.R.drawable.ic_menu_revert)
        val uninstallMenu = menu.addSubMenu(MENU_GROUP_ID, MENU_UNINSTALL, Menu.NONE,
                R.string.uninstall_app)
        uninstallMenu.setIcon(android.R.drawable.ic_menu_delete)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_CLEAN_LOG -> txtLog!!.text = ""
            MENU_UNINSTALL -> PackageUtils.uninstall(context, context!!.packageName)
            else -> {
            }
        }
        return super.onMenuItemSelected(featureId, item)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        clickedPosition = position
        if (isMain) {
            performMainItemClick()
        } else {
            performSubItemClick()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackKeyClick()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun onBackKeyClick() {
        if (isMain) {
            System.exit(0)
        } else {
            isMain = true
            displayIntroduction()
            adapter!!.refreshView(ListViewAdapter.Companion.INDEX_NONE)
            setListViewSelection()
            actionCallback!!.sendResponse(context!!.getString(R.string.test_end))
        }
    }

    private fun setListViewSelection() {
        lvwTestItems!!.adapter = adapter
        lvwTestItems!!.setSelection(scrollPosition)
    }

    private fun performMainItemClick() {
        isMain = false
        clickedMainItem = MainApplication.Companion.testItems!!.get(clickedPosition)
        actionCallback!!.sendResponse(context!!.getString(R.string.welcome_to)
                + "\t" + clickedMainItem!!.getDisplayName(LanguageHelper.getLanguageType(context)))
        displayIntroduction()
        if (clickedMainItem!!.isActivity()) {
            // if the test item is a activity, jump by package-name property.
            val cn = ComponentName(context,
                    clickedMainItem!!.packageName)
            val intent = Intent()
            intent.component = cn
            intent.putExtra(Constants.MAIN_ITEM,
                    clickedMainItem!!.command)
            startActivityForResult(intent, clickedPosition)
        } else {
            // otherwise jump to SubItem page and automatically execute autoTest
            // item if exsies
            adapter!!.refreshView(clickedPosition)
            lvwTestItems!!.setSelection(0)
            lvwTestItems!!.adapter = adapter
            // setLayoutIntroductionIfExists();
        }
    }

    private fun performSubItemClick() {
        testParameters!!.clear()
        testParameters!![Constants.MAIN_ITEM] = clickedMainItem!!.command
        val subItemCommand = clickedMainItem!!.getSubItem(clickedPosition).command
        testParameters!![Constants.SUB_ITEM] = subItemCommand
        Log.e(TAG, "itemPressed : " + clickedMainItem!!.command + "/" + subItemCommand)
        ActionManager.Companion.doSubmit(clickedMainItem!!.command + "/" + subItemCommand,
                context, testParameters, actionCallback)
    }

    /**
     * 显示Introduction信息<br></br>
     * 如果isMain == true,则隐藏,否则显示
     */
    private fun displayIntroduction() {
//        if (txtIntroduction != null) {
//            if (isMain) {
//                txtIntroduction!!.visibility = View.GONE
//            } else {
//                txtIntroduction!!.visibility = View.VISIBLE
//                txtIntroduction!!.text = """
//                    ${context!!.getString(R.string.welcome_to)}
//                    ${clickedMainItem!!.getDisplayName(LanguageHelper.getLanguageType(context))}
//                    """.trimIndent()
//            }
//        }
    }

    private val onTestItemsScrollListener: AbsListView.OnScrollListener = object : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            // position which records the position of the visible top line
            if (isMain) {
                scrollPosition = lvwTestItems!!.firstVisiblePosition
            }
        }

        override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int,
                              totalItemCount: Int) {
        }
    }
    private val handlerCallback = Handler.Callback { msg ->
        when (msg.what) {
            Constants.HANDLER_LOG -> LogHelper.infoAppendMsg(msg.obj as String, txtLog)
            Constants.HANDLER_LOG_SUCCESS -> LogHelper.infoAppendMsgForSuccess(msg.obj as String, txtLog)
            Constants.HANDLER_LOG_FAILED -> LogHelper.infoAppendMsgForFailed(msg.obj as String, txtLog)
            else -> {
            }
        }
        true
    }

    companion object {
        private const val TAG = "DEBUG"
        private const val MENU_CLEAN_LOG = Menu.FIRST
        private const val MENU_UNINSTALL = Menu.FIRST + 1
        private const val MENU_GROUP_ID = 0
    }
}