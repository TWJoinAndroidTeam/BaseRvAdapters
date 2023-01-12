package com.example.baseadapterslibrary.adapter.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate

abstract class BasePagingLoadStateAdapter<VB : ViewBinding>(private val inflate: Inflate<VB>) : LoadStateAdapter<LifecycleOwnerBindHolder>() {

    lateinit var context: Context

    val isContextInitialized get() = this::context.isInitialized

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LifecycleOwnerBindHolder {
        if (!isContextInitialized) context = parent.context
        return getViewHolder(parent, loadState).apply {
            createHolder(binding as VB, this)
        }
    }

    protected open fun getViewHolder(parent: ViewGroup, loadState: LoadState): LifecycleOwnerBindHolder {
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

    abstract fun createHolder(binding: VB, viewHolder: RecyclerView.ViewHolder)
    abstract fun bind(binding: VB, loadState: LoadState)

}
