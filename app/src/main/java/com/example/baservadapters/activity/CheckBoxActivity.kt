package com.example.baservadapters.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.model.CheckBoxModel
import com.example.baseadapterslibrary.model.ChooserMode
import com.example.baseadapterslibrary.model.SelectLimitOption
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.adapter.DemoRvAdapter
import com.example.baservadapters.adapter.TestMutiCheckBoxAdapter
import com.example.baservadapters.adapter.TestSingleCheckBoxAdapter
import com.example.baservadapters.databinding.ActivityCheckBoxBinding
import com.example.baservadapters.util.DimensionUtil
import kotlinx.coroutines.launch

class CheckBoxActivity : AppCompatActivity() {

    companion object {
        private const val TYPE_SINGLE = "single"
        private const val TYPE_MUTI = "muti"
        private const val TYPE_MUTI_MAX_3 = "max 3"
    }

    private var testSingleCheckBoxAdapter: TestSingleCheckBoxAdapter? = null

    private var testMutiCheckBoxAdapter: TestMutiCheckBoxAdapter? = null

    private var testMutiMaxThreeCheckBoxAdapter: TestMutiCheckBoxAdapter? = null

    private lateinit var viewBinding: ActivityCheckBoxBinding

    private var itemTypeAdapter: DemoRvAdapter? = null

    private val testSize = 10

    private val listOptions = mutableListOf("single", "muti", "max 3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityCheckBoxBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        initRv()

        setData()
    }

    private fun initRv() {
        viewBinding.rvMain.apply {
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                RvDecoration(
                    horizontalSpace = DimensionUtil.dp2px(8),
                    verticalSpace = DimensionUtil.dp2px(8),
                    margin = DimensionUtil.dp2px(16)
                )
            )

            testSingleCheckBoxAdapter = TestSingleCheckBoxAdapter()

            testMutiCheckBoxAdapter = TestMutiCheckBoxAdapter(ChooserMode.MultipleResponse())

            testMutiMaxThreeCheckBoxAdapter = TestMutiCheckBoxAdapter(ChooserMode.MultipleResponse(SelectLimitOption(3, true)))

            adapter = testMutiMaxThreeCheckBoxAdapter
        }

        viewBinding.rvOption.apply {
            layoutManager = GridLayoutManager(this@CheckBoxActivity, listOptions.size)
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

            when (listOptions[it]) {
                TYPE_SINGLE -> {
                    viewBinding.rvMain.adapter = testSingleCheckBoxAdapter
                }

                TYPE_MUTI -> {
                    viewBinding.rvMain.adapter = testMutiCheckBoxAdapter
                }

                TYPE_MUTI_MAX_3 -> {
                    viewBinding.rvMain.adapter = testMutiMaxThreeCheckBoxAdapter
                }
            }
        }
    }

    private fun setData() {

        val testSingleList = mutableListOf<CheckBoxModel<Int>>()
        val testMutiList = mutableListOf<CheckBoxModel<Int>>()
        val testMaxThreeList = mutableListOf<CheckBoxModel<Int>>()


        for (i in 0 until testSize) {
            testMutiList.add(CheckBoxModel(i, i < 3))
            testMaxThreeList.add(CheckBoxModel(i, i < 3))
            testSingleList.add(CheckBoxModel(i, i == 1))
        }

        lifecycleScope.launch {
            itemTypeAdapter?.updateDataSet(listOptions)
            testSingleCheckBoxAdapter?.updateDataSet(testSingleList)
            testMutiCheckBoxAdapter?.updateDataSet(testMutiList)
            testMutiMaxThreeCheckBoxAdapter?.updateDataSet(testMaxThreeList)
        }
    }
}