package com.example.baservadapters.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baservadapters.databinding.ActivityMainBinding
import com.example.baservadapters.util.DimensionUtil
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.adapter.DemoRvAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private var itemTypeAdapter: DemoRvAdapter? = null

    private val optionList = mutableListOf(TYPE_HEADER_OR_FOOTER, TYPE_CHECKBOX)

    companion object {

        const val TYPE_HEADER_OR_FOOTER = "header or Footer"
        const val TYPE_CHECKBOX = "checkbox"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        initRv()

        setData()
    }

    private fun initRv() {

        Log.e("???", "initRv")

        viewBinding.rv.apply {
//            layoutManager = LinearLayoutManager(context)
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
            }
        }

    }

    private fun setData() {

        lifecycleScope.launch {
            itemTypeAdapter?.updateDataSet(optionList)
        }
    }

}