package com.example.baseadapterslibrary.extension

import androidx.recyclerview.widget.ConcatAdapter
import com.example.baseadapterslibrary.adapter.normal.BaseRvAdapter
import com.example.baseadapterslibrary.adapter.normal.HeaderOrFooterAdapter


fun BaseRvAdapter<*, *>?.withFooterAdapter(footerAdapter: HeaderOrFooterAdapter<*>?): ConcatAdapter {
    this?.setLoadStateAdapterListener { loadStates ->

        footerAdapter?.loadState = loadStates
    }
    return ConcatAdapter(this, footerAdapter)
}

fun BaseRvAdapter<*, *>?.withHeaderAdapter(headerAdapter: HeaderOrFooterAdapter<*>?): ConcatAdapter {
    this?.setLoadStateAdapterListener { loadStates ->
        headerAdapter?.loadState = loadStates

    }
    return ConcatAdapter(headerAdapter, this)
}

fun BaseRvAdapter<*, *>?.withHeaderAndFooterAdapter(headerAdapter: HeaderOrFooterAdapter<*>?, footerAdapter: HeaderOrFooterAdapter<*>?): ConcatAdapter {
    this?.setLoadStateAdapterListener { loadStates ->
        headerAdapter?.loadState = loadStates
        footerAdapter?.loadState = loadStates
    }
    return ConcatAdapter(headerAdapter, this, footerAdapter)
}