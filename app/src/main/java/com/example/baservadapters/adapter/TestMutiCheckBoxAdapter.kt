package com.example.baservadapters.adapter

import androidx.core.content.ContextCompat
import com.example.baseadapterslibrary.adapter.normal.checkbox.CheckBoxAdapter
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.model.CheckBoxModel
import com.example.baseadapterslibrary.model.ChooserMode
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import com.example.baservadapters.databinding.ItemCheckboxBinding

class TestMutiCheckBoxAdapter(override val chooserMode: ChooserMode) : CheckBoxAdapter<ItemCheckboxBinding, CheckBoxModel<Int>>() {

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemCheckboxBinding> {
        return ItemCheckboxBinding::inflate
    }

    override fun doWhenCreateHolder(binding: ItemCheckboxBinding, viewHolder: BaseViewBindHolder) {

    }

    override fun doWhenBindHolder(binding: ItemCheckboxBinding, checkBox: CheckBoxModel<Int>, position: Int, viewHolder: BaseViewBindHolder) {
        binding.txtNumber.text = checkBox.item.toString()
        binding.root.setOnClickListener {
            clickCheckBox(checkBox, position)
        }
    }

    override fun onCheckStateExchange(isCheck: Boolean, binding: ItemCheckboxBinding, checkBox: CheckBoxModel<Int>, position: Int) {
        if (isCheck) {
            binding.txtNumber.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            binding.root.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black))
        } else {
            binding.txtNumber.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            binding.root.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }
    }

    override fun doWhenBindPayload(payload: Any, binding: ItemCheckboxBinding, item: CheckBoxModel<Int>, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {

    }
}
