package com.example.baservadapters.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.adapter.normal.BaseRvAdapter
import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import com.example.baservadapters.databinding.ItemButtonBinding

class ItemButtonRvAdapter : BaseRvAdapter<ItemButtonBinding, String>() {

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemButtonBinding> {
        return ItemButtonBinding::inflate
    }

    override fun createHolder(binding: ItemButtonBinding, viewHolder: RecyclerView.ViewHolder) {
        binding.btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
    }

    override fun bind(binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {
        binding.btn.text = item
    }

    override fun partBind(payload: Any, binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {

    }
}