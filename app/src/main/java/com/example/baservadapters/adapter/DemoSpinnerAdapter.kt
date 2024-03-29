package com.example.baservadapters.adapter

import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baseadapterslibrary.adapter.normal.spinner.ISpinnerUI
import com.example.baseadapterslibrary.adapter.normal.spinner.SpinnerAdapter
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import com.example.baservadapters.databinding.ItemDemoStyleSpinnerBinding
import com.example.baservadapters.model.TestSpinner

class DemoSpinnerAdapter : SpinnerAdapter<ItemDemoStyleSpinnerBinding, TestSpinner>() {

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemDemoStyleSpinnerBinding> {
        return ItemDemoStyleSpinnerBinding::inflate
    }

    override fun doWhenBindPayload(payload: Any, binding: ItemDemoStyleSpinnerBinding, item: TestSpinner, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {

    }

    override fun doWhenBindHolder(binding: ItemDemoStyleSpinnerBinding, item: TestSpinner, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {
        binding.txtTitle.text = item.spinnerItemName
        binding.root.setOnClickListener {
            onSpinnerItemClick(item, bindingAdapterPosition)
        }
    }

    override fun doWhenCreateHolder(binding: ItemDemoStyleSpinnerBinding, viewHolder: BaseViewBindHolder) {

    }
}