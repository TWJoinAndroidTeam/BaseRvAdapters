package com.example.baseadapterslibrary.adapter.paging.checkbox

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.model.IPagingExpandableCheckBox

abstract class ExpandableCheckBoxPagingAdapter<VB : ViewBinding, ECB : IPagingExpandableCheckBox>(diffCallback: DiffUtil.ItemCallback<ECB>) : CheckBoxPagingAdapter<VB, ECB>(diffCallback) {


}