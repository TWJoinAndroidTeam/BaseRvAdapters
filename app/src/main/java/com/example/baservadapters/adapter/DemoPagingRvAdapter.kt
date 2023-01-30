package com.example.baservadapters.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.adapter.paging.BasePagingRvAdapter
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

    override fun createHolder(binding: ItemCheckboxBinding, viewHolder: RecyclerView.ViewHolder) {

    }

    override fun bind(binding: ItemCheckboxBinding, item: String, position: Int) {
        onItemClickListener?.invoke(item, position)
    }

    override fun partBind(payload: Any, binding: ItemCheckboxBinding, item: String, position: Int) {

    }
}