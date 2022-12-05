package com.example.baseadapterslibrary.adapter.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate

abstract class BasePagingLoadStateAdapter<VB : ViewBinding>(private val inflate: Inflate<VB>) : LoadStateAdapter<LifecycleOwnerBindHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LifecycleOwnerBindHolder {
        if (!::context.isInitialized) context = parent.context
        return LifecycleOwnerBindHolder(
            inflate.invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LifecycleOwnerBindHolder, loadState: LoadState) {
        bind(holder.binding as VB, loadState)
    }


    abstract fun bind(binding: VB, loadState: LoadState)

}
