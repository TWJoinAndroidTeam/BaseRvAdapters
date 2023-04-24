package com.example.baservadapters.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.adapter.normal.BaseRvAdapter
import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import com.example.baservadapters.databinding.ItemButtonBinding

class DemoRvAdapter : BaseRvAdapter<ItemButtonBinding, String>() {

    private var clickListener: ((position: Int) -> Unit)? = null

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemButtonBinding> {
        return ItemButtonBinding::inflate
    }

    override fun createHolder(binding: ItemButtonBinding, viewHolder: RecyclerView.ViewHolder) {
        binding.btn.setOnClickListener {
            clickListener!!.invoke(viewHolder.bindingAdapterPosition)
        }
    }

    override fun doWhenBindHolder(binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {
        binding.btn.text = item
    }

    override fun doWhenBindPayload(payload: Any, binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder) {

    }

    fun setOnClickListener(listener: (position: Int) -> Unit) {
        this.clickListener = listener
    }

}
