package com.example.baservadapters.activity

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.adapter.normal.checkbox.CheckBoxAdapter
import com.example.baseadapterslibrary.baseAdaptersLibrary.module.CheckBoxModel
import com.example.baseadapterslibrary.module.ChooserMode
import com.example.baservadapters.databinding.ItemCheckboxBinding

class TestCheckBoxAdapter : CheckBoxAdapter<ItemCheckboxBinding, CheckBoxModel<Int>>(ItemCheckboxBinding::inflate) {

    override val chooserMode: ChooserMode
        get() = ChooserMode.MultipleResponse(3)

    override fun createHolder(binding: ItemCheckboxBinding, viewHolder: RecyclerView.ViewHolder) {

    }

    override fun bind(binding: ItemCheckboxBinding, checkBox: CheckBoxModel<Int>, position: Int, viewHolder: CheckBoxBindHolder) {
        binding.txtNumber.text = checkBox.item.toString()
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


}
