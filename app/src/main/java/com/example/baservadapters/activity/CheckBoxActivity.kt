package com.example.baservadapters.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.baseAdaptersLibrary.module.CheckBoxModel
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.adapter.TestCheckBoxAdapter
import com.example.baservadapters.databinding.ActivityCheckBoxBinding
import com.example.baservadapters.util.DimensionUtil
import kotlinx.coroutines.launch

class CheckBoxActivity : AppCompatActivity() {

    private var testAdapter: TestCheckBoxAdapter? = null

    private lateinit var viewBinding: ActivityCheckBoxBinding

    private val testSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityCheckBoxBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        initRv()

        setData()
    }

    private fun initRv() {
        viewBinding.rv.apply {
//            layoutManager = LinearLayoutManager(context)
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                RvDecoration(
                    horizontalSpace = DimensionUtil.dp2px(8),
                    verticalSpace = DimensionUtil.dp2px(8),
                    margin = DimensionUtil.dp2px(16)
                )
            )
            testAdapter = TestCheckBoxAdapter()
            adapter = testAdapter
        }
    }

    private fun setData() {

        val testList = mutableListOf<CheckBoxModel<Int>>()

        for (i in 0 until testSize) {
            testList.add(CheckBoxModel(i, false))
        }

        lifecycleScope.launch {
            testAdapter?.setData(testList)
        }
    }
}