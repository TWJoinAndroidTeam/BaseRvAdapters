package com.example.baservadapters.adapter

import android.view.View
import com.example.baseadapterslibrary.adapter.normal.HeaderOrFooterAdapter
import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baseadapterslibrary.model.NormalRvLoadState
import com.example.baservadapters.databinding.ItemTextBinding

class FooterRvAdapter : HeaderOrFooterAdapter<ItemTextBinding>() {
    override fun getViewBindingInflate(viewType: Int): Inflate<ItemTextBinding> {
        return ItemTextBinding::inflate
    }

    override fun createHolder(binding: ItemTextBinding, loadState: NormalRvLoadState) {

    }

    override fun bind(binding: ItemTextBinding, loadState: NormalRvLoadState) {
        when (loadState) {
            NormalRvLoadState.HaveData -> {
                binding.root.visibility = View.VISIBLE
                binding.txtItem.text = "- footer -"
            }
            is NormalRvLoadState.NoData -> {
                if (loadState.isFinishAdd) {
                    binding.root.visibility = View.VISIBLE
                    binding.txtItem.text = "- footer no data -"
                } else {
                    binding.txtItem.text = "- header not init data -"
                }
            }
        }
    }

}