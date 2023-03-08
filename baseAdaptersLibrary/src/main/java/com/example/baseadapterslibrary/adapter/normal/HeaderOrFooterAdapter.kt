package com.example.baseadapterslibrary.adapter.normal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.module.NormalRvLoadState
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder

abstract class HeaderOrFooterAdapter<VB : ViewBinding> : RecyclerView.Adapter<BaseViewBindHolder>() {

    abstract fun getViewBindingInflate(viewType: Int): Inflate<VB>

    val isContextInitialized get() = this::context.isInitialized

    lateinit var context: Context

    /**
     * LoadState to present in the adapter.
     *
     * Changing this property will immediately notify the Adapter to change the item it's
     * presenting.
     */
    var loadState: NormalRvLoadState = NormalRvLoadState.NoData(false)
        set(loadState) {
            if (field != loadState) {
                val oldItem = displayLoadStateAsItem(field)
                val newItem = displayLoadStateAsItem(loadState)

                if (oldItem && !newItem) {
                    notifyItemRemoved(0)
                } else if (newItem && !oldItem) {
                    notifyItemInserted(0)
                } else if (oldItem && newItem) {
                    notifyItemChanged(0)
                }
                field = loadState
            }
        }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewBindHolder {
        if (!isContextInitialized) context = parent.context

        return getViewHolder(parent, viewType).apply {
            createHolder(binding as VB, loadState)
        }
    }

    final override fun onBindViewHolder(holder: BaseViewBindHolder, position: Int) {
        bind(holder.binding as VB, loadState)
    }

    final override fun getItemViewType(position: Int): Int = getStateViewType(loadState)

    final override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0


    abstract fun createHolder(binding: VB, loadState: NormalRvLoadState)

    abstract fun bind(binding: VB, loadState: NormalRvLoadState)

    /**
     * Override this method to use different view types per LoadState.
     *
     * By default, this LoadStateAdapter only uses a single view type.
     */
    open fun getStateViewType(loadState: NormalRvLoadState): Int = 0

    /**
     * Returns true if the LoadState should be displayed as a list item when active.
     *
     * By default, [LoadState.Loading] and [LoadState.Error] present as list items, others do not.
     */
    open fun displayLoadStateAsItem(loadState: NormalRvLoadState): Boolean {
        return true
    }

    protected open fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewBindHolder {
        return BaseViewBindHolder(
            getViewBindingInflate(viewType).invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
