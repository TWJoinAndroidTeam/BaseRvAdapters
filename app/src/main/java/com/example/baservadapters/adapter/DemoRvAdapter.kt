package com.example.baservadapters.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.adapter.normal.BaseRvAdapter
import com.example.baseadapterslibrary.adapter.normal.Inflate
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

    override fun bind(binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseBindHolder) {
        binding.btn.text = item
    }

    override fun partBind(payload: Any, binding: ItemButtonBinding, item: String, bindingAdapterPosition: Int, viewHolder: BaseBindHolder) {

    }

    fun setOnClickListener(listener: (position: Int) -> Unit) {
        this.clickListener = listener
    }

}
