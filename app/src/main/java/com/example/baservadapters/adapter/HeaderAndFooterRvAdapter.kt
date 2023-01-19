package com.example.baservadapters.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.BaseHeadOrFooterRvAdapter
import com.example.baseadapterslibrary.adapter.normal.HeaderOrFooterRVType
import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baservadapters.databinding.ItemButtonBinding
import com.example.baservadapters.databinding.ItemTextBinding

class HeaderAndFooterRvAdapter : BaseHeadOrFooterRvAdapter<ItemButtonBinding, String>() {
    override val type: HeaderOrFooterRVType
        get() = HeaderOrFooterRVType.HeaderAndFooter

    override fun getHeaderView(parent: ViewGroup): ViewBinding? {
        return ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            txtItem.text = "- header -"
        }
    }

    override fun getFooterView(parent: ViewGroup): ViewBinding {
        return ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            txtItem.text = "- footer -"
        }
    }

    override fun getNormalItemViewType(position: Int): Int? {
        return null
    }

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemButtonBinding> {
        return ItemButtonBinding::inflate
    }

    override fun createHolder(binding: ItemButtonBinding, viewHolder: RecyclerView.ViewHolder) {
        binding.btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
    }

    override fun bind(binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseBindHolder) {
        binding.btn.text = item
    }

    override fun partBind(payload: Any, binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseBindHolder) {

    }
}