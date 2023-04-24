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

    override fun doWhenCreateHolder(binding: ItemButtonBinding, viewHolder: BaseViewBindHolder) {
        binding.btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
    }

    override fun doWhenBindHolder(binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {
        binding.btn.text = item
    }

    override fun doWhenBindPayload(payload: Any, binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {

    }
}