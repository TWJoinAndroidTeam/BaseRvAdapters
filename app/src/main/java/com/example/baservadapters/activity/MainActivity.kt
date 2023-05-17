package com.example.baservadapters.activity

import android.content.Intent
import android.os.Bundle
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.adapter.normal.spinner.SpinnerBuilder
import com.example.baservadapters.databinding.ActivityMainBinding
import com.example.baservadapters.util.DimensionUtil
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.R
import com.example.baservadapters.activity.expandablecheckbox.ExpandableCheckBoxActivity
import com.example.baservadapters.adapter.DemoRvAdapter
import com.example.baservadapters.adapter.DemoSpinnerAdapter
import com.example.baservadapters.model.TestSpinner
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val TYPE_HEADER_OR_FOOTER = "header or Footer"
        const val TYPE_CHECKBOX = "checkbox"
        const val TYPE_EXPAND_CHECKBOX = "expand checkbox"
    }

    private lateinit var viewBinding: ActivityMainBinding

    private var itemTypeAdapter: DemoRvAdapter? = null

    private val optionList = mutableListOf(TYPE_HEADER_OR_FOOTER, TYPE_CHECKBOX, TYPE_EXPAND_CHECKBOX)

    private lateinit var spinnerAdapter: DemoSpinnerAdapter

    private lateinit var spinnerWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        initRv()

        initSpinner()

        viewBinding.btnTest.setOnClickListener {
            spinnerWindow.width = it.width
            spinnerWindow.showAsDropDown(it)
        }

        spinnerAdapter.addOnSpinnerItemSelectListener { item, position ->
            viewBinding.txtSelectSpinner.text = item.spinnerItemName
        }

        setData()
    }

    private fun initSpinner() {
        spinnerAdapter = DemoSpinnerAdapter()
        spinnerWindow = SpinnerBuilder(this, spinnerAdapter)
            .setBackgroundRes(R.drawable.bg_white_stroke_1_purple_732ef5_radius_5)
            .build().first
    }

    private fun initRv() {

        viewBinding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                RvDecoration(
                    horizontalSpace = DimensionUtil.dp2px(8),
                    verticalSpace = DimensionUtil.dp2px(8),
                    margin = DimensionUtil.dp2px(16)
                )
            )
            itemTypeAdapter = DemoRvAdapter()
            adapter = itemTypeAdapter
        }

        itemTypeAdapter?.setOnClickListener {

            when (optionList[it]) {
                TYPE_HEADER_OR_FOOTER -> {
                    val intent = Intent(this, HeaderOrFooterActivity::class.java)
                    startActivity(intent)
                }

                TYPE_CHECKBOX -> {
                    val intent = Intent(this, CheckBoxActivity::class.java)
                    startActivity(intent)
                }

                TYPE_EXPAND_CHECKBOX -> {
                    val intent = Intent(this, ExpandableCheckBoxActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    private fun setData() {

        val testSpinnerModelList = listOf(
            TestSpinner(true, 1),
            TestSpinner(true, 2),
            TestSpinner(true, 3)
        )

        lifecycleScope.launch {
            itemTypeAdapter?.updateDataSet(optionList)
            spinnerAdapter.updateDataSet(testSpinnerModelList.toMutableList())
        }
    }

}