package com.example.baservadapters.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.adapter.DemoRvAdapter
import com.example.baservadapters.adapter.FooterRvAdapter
import com.example.baservadapters.adapter.HeaderAndFooterRvAdapter
import com.example.baservadapters.adapter.HeaderRvAdapter
import com.example.baservadapters.databinding.ActivityHeaderOrFooterBinding
import com.example.baservadapters.util.DimensionUtil
import kotlinx.coroutines.launch

class HeaderOrFooterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeaderOrFooterBinding

    private var itemTypeAdapter: DemoRvAdapter? = null
    private var footerRvAdapter: FooterRvAdapter? = null
    private var headerRvAdapter: HeaderRvAdapter? = null
    private var headerAndFooterRvAdapter: HeaderAndFooterRvAdapter? = null

    private val optionList = mutableListOf(TYPE_HEADER, TYPE_FOOTER, TYPE_HEADER_AND_FOOTER)

    private val testSize = 20

    companion object {
        const val TYPE_HEADER = "header"
        const val TYPE_FOOTER = "footer"
        const val TYPE_HEADER_AND_FOOTER = "header and footer"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeaderOrFooterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRv()

        setData()
    }

    private fun initRv() {

        Log.e("???", "initRv")

        binding.rvOption.apply {
            layoutManager = GridLayoutManager(this@HeaderOrFooterActivity, 3)
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
                TYPE_HEADER -> {
                    binding.rvList.adapter = headerRvAdapter
                }

                TYPE_FOOTER -> {
                    binding.rvList.adapter = footerRvAdapter
                }

                TYPE_HEADER_AND_FOOTER -> {
                    binding.rvList.adapter = headerAndFooterRvAdapter
                }
            }
        }

        headerRvAdapter = HeaderRvAdapter()
        footerRvAdapter = FooterRvAdapter()
        headerAndFooterRvAdapter = HeaderAndFooterRvAdapter()

        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@HeaderOrFooterActivity)
            addItemDecoration(
                RvDecoration(
                    horizontalSpace = DimensionUtil.dp2px(8),
                    verticalSpace = DimensionUtil.dp2px(8),
                    margin = DimensionUtil.dp2px(16)
                )
            )

            adapter = headerRvAdapter
        }

    }

    private fun setData() {

        val testList = mutableListOf<String>()

        for (i in 0 until testSize) {
            testList.add(i.toString())
        }

        lifecycleScope.launch {
            headerRvAdapter?.updateDataSet(testList)
            footerRvAdapter?.updateDataSet(testList)
            headerAndFooterRvAdapter?.updateDataSet(testList)
            itemTypeAdapter?.updateDataSet(optionList)
        }
    }
}