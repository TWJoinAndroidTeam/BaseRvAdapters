package com.example.baservadapters.activity.expandablecheckbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import com.example.baservadapters.databinding.ActivityExpandableCheckBoxBinding
import com.example.baservadapters.util.DimensionUtil
import kotlinx.coroutines.launch

class ExpandableCheckBoxActivity : AppCompatActivity() {
    private var isCheckAll = false
    private var isExpandAll = false

    val adapter: MyExpandableCheckBoxAdapter by lazy { MyExpandableCheckBoxAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityExpandableCheckBoxBinding.inflate(layoutInflater).apply {

            initRv(this)

            initBtn(this)

            setContentView(root)
        }

    }

    private fun initRv(binding: ActivityExpandableCheckBoxBinding) {
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@ExpandableCheckBoxActivity)

            addItemDecoration(
                RvDecoration(
                    horizontalSpace = DimensionUtil.dp2px(8),
                    verticalSpace = DimensionUtil.dp2px(8),
                    margin = DimensionUtil.dp2px(16)
                )
            )

            adapter = this@ExpandableCheckBoxActivity.adapter.apply {
                lifecycleScope.launch {

                    updateDataSet(MutableList(10) {
                        TestCB(isExpand = it % 2 == 0)
                    })
                }
            }
        }
    }

    private fun initBtn(binding: ActivityExpandableCheckBoxBinding) {
        binding.apply {
            btnCheck.setOnClickListener {
                if (isCheckAll) {
                    adapter.clearAll()

                } else {
                    adapter.selectAll()
                }
                isCheckAll = !isCheckAll
            }

            btnExpand.setOnClickListener {
                isExpandAll = !isExpandAll
                adapter.changeExpandAllData(isExpandAll)
            }
        }
    }
}