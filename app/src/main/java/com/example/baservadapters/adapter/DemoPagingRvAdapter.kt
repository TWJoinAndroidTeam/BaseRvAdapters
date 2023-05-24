package com.example.baservadapters.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.adapter.paging.BasePagingRvAdapter
import com.example.baseadapterslibrary.view_holder.LifecycleOwnerViewBindHolder
import com.example.baservadapters.databinding.ItemCheckboxBinding

class DemoPagingRvAdapter : BasePagingRvAdapter<ItemCheckboxBinding, String>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean = oldItem == newItem
        }

    }

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemCheckboxBinding> {
        return ItemCheckboxBinding::inflate
    }


    override fun doWhenCreateViewHolder(binding: ItemCheckboxBinding, viewHolder: LifecycleOwnerViewBindHolder) {

    }


    override fun doWhenBindHolder(binding: ItemCheckboxBinding, item: String, position: Int) {
        onItemClickCallback?.invoke(item)
    }

    override fun doWhenBindPayload(payload: Any, binding: ItemCheckboxBinding, item: String, position: Int) {

    }
}