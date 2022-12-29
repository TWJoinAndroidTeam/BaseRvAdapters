package com.example.baservadapters.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.adapter.normal.checkbox.CheckBoxAdapter
import com.example.baseadapterslibrary.baseAdaptersLibrary.module.CheckBoxModel
import com.example.baservadapters.databinding.ActivityMainBinding
import com.example.baservadapters.databinding.ItemCheckboxBinding
import com.example.baservadapters.util.DimensionUtil
import com.fpcc.formosa.common.view.recyclerview_decoration.RvDecoration
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private var testAdapter: CheckBoxAdapter<ItemCheckboxBinding, CheckBoxModel<Int>>? = null

    private val testSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("???","create")

        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        initRv()

        setData()
    }

    private fun initRv() {

        Log.e("???","initRv")

        viewBinding.rv.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                RvDecoration(
                    topMargin = DimensionUtil.dp2px(10),
                    bottomMargin = DimensionUtil.dp2px(10),
                    rightMargin = DimensionUtil.dp2px(20),
                    leftMargin = DimensionUtil.dp2px(20)
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