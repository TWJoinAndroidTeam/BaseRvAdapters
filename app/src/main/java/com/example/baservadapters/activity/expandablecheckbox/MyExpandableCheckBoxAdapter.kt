package com.example.baservadapters.activity.expandablecheckbox

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baseadapterslibrary.adapter.normal.checkbox.ExpandableCheckBoxAdapter
import com.example.baseadapterslibrary.model.ChooserMode
import com.example.baseadapterslibrary.model.IExpandableCheckBox
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import com.example.baservadapters.databinding.ItemMyExpandableCheckBoxBinding

class TestCB(
    override var isExpand: Boolean=true,
    override var isCheck: Boolean= false
) : IExpandableCheckBox

class MyExpandableCheckBoxAdapter : ExpandableCheckBoxAdapter<ItemMyExpandableCheckBoxBinding, TestCB>(false) {
    override fun onExpandChange(viewBinding: ItemMyExpandableCheckBoxBinding, isExpand: Boolean, cb: TestCB, adapterPosition: Int) {
        viewBinding.view2.visibility = if (isExpand) View.VISIBLE else View.GONE
    }

    override val chooserMode: ChooserMode
        get() = ChooserMode.MultipleResponse()

    override fun onCheckStateExchange(isCheck: Boolean, binding: ItemMyExpandableCheckBoxBinding, checkBox: TestCB, position: Int) {
        binding.checkBox.isChecked = isCheck
    }

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemMyExpandableCheckBoxBinding> = ItemMyExpandableCheckBoxBinding::inflate

    override fun doWhenBindPayload(payload: Any, binding: ItemMyExpandableCheckBoxBinding, item: TestCB, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {}

    @SuppressLint("ClickableViewAccessibility")
    override fun doWhenBindHolder(binding: ItemMyExpandableCheckBoxBinding, item: TestCB, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {

        binding.checkBox.setOnTouchListener { _, event ->
            // 判断是否为点击事件
            if (event.action == MotionEvent.ACTION_UP) {
                clickCheckBox(item, bindingAdapterPosition)
            }
            // 返回true表示已处理事件，不需要继续传递
            true
        }

        binding.view.setOnClickListener {
            clickExpandItem(binding, bindingAdapterPosition)
        }
    }

    override fun doWhenCreateHolder(binding: ItemMyExpandableCheckBoxBinding, viewHolder: BaseViewBindHolder) {}
}