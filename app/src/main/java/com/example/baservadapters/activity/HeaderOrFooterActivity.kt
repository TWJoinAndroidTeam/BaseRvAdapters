package com.example.baservadapters.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.extension.withFooterAdapter
import com.example.baseadapterslibrary.extension.withHeaderAdapter
import com.example.baseadapterslibrary.extension.withHeaderAndFooterAdapter
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.adapter.DemoRvAdapter
import com.example.baservadapters.adapter.FooterRvAdapter
import com.example.baservadapters.adapter.ItemButtonRvAdapter
import com.example.baservadapters.adapter.HeaderRvAdapter
import com.example.baservadapters.databinding.ActivityHeaderOrFooterBinding
import com.example.baservadapters.util.DimensionUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HeaderOrFooterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHeaderOrFooterBinding

    private var itemTypeAdapter: DemoRvAdapter? = null
    private var footerRvAdapter: FooterRvAdapter? = null
    private var headerRvAdapter: HeaderRvAdapter? = null
    private var headerAndFooterRvAdapter: ItemButtonRvAdapter? = null

    val testList = mutableListOf<String>()

    val emptyList = mutableListOf<String>()

    private val optionList = mutableListOf(TYPE_HEADER, TYPE_FOOTER, TYPE_HEADER_AND_FOOTER)

    private val testSize = 20

    val list: List<MutableList<String>> = listOf(emptyList, testList)

    companion object {
        private const val TYPE_HEADER = "header"
        private const val TYPE_FOOTER = "footer"
        private const val TYPE_HEADER_AND_FOOTER = "header and footer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeaderOrFooterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRv()

        lifecycleScope.launch {
            setData()
        }
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
                    binding.rvList.adapter = headerAndFooterRvAdapter?.withHeaderAdapter(headerRvAdapter)
                }

                TYPE_FOOTER -> {
                    binding.rvList.adapter = headerAndFooterRvAdapter?.withFooterAdapter(footerRvAdapter)
                }

                TYPE_HEADER_AND_FOOTER -> {
                    binding.rvList.adapter = headerAndFooterRvAdapter?.withHeaderAndFooterAdapter(headerRvAdapter, footerRvAdapter)
                }
            }

            lifecycleScope.launch {
                setData()
            }
        }

        headerRvAdapter = HeaderRvAdapter()
        footerRvAdapter = FooterRvAdapter()
        headerAndFooterRvAdapter = ItemButtonRvAdapter()

        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@HeaderOrFooterActivity)
            addItemDecoration(
                RvDecoration(
                    horizontalSpace = DimensionUtil.dp2px(8),
                    verticalSpace = DimensionUtil.dp2px(8),
                    margin = DimensionUtil.dp2px(16)
                )
            )

            adapter = headerAndFooterRvAdapter.withHeaderAdapter(headerRvAdapter)
        }

        lifecycleScope.launch {
            itemTypeAdapter?.updateDataSet(optionList)
            setData()
        }
    }

    private suspend fun setData() {
        testList.clear()

        for (i in 0 until testSize) {
            testList.add(i.toString())
        }

        delay(500)

        headerAndFooterRvAdapter?.updateDataSet(list.random())

    }
}