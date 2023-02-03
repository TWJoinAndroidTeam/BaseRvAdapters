package com.example.baservadapters.adapter

import android.view.View
import com.example.baseadapterslibrary.adapter.normal.HeaderOrFooterAdapter
import com.example.baseadapterslibrary.adapter.normal.Inflate
import com.example.baseadapterslibrary.module.NormalRvLoadState
import com.example.baservadapters.databinding.ItemTextBinding

class HeaderRvAdapter : HeaderOrFooterAdapter<ItemTextBinding>() {

    override fun getViewBindingInflate(viewType: Int): Inflate<ItemTextBinding> {
        return ItemTextBinding::inflate
    }

    override fun createHolder(binding: ItemTextBinding, loadState: NormalRvLoadState) {

    }

    override fun bind(binding: ItemTextBinding, loadState: NormalRvLoadState) {
        when (loadState) {
            NormalRvLoadState.HaveData -> {

                binding.txtItem.text = "- header -"
            }
            is NormalRvLoadState.NoData -> {
                if (loadState.isFinishAdd) {
                    binding.txtItem.text = "- header no data -"
                } else {
                    binding.txtItem.text = "- header not init data -"
                }
            }
        }
    }


}