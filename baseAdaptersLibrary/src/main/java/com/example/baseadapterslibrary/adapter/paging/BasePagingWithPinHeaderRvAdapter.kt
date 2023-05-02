package com.example.baseadapterslibrary.adapter.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.model.IHeaderAdapterSetting
import com.example.baseadapterslibrary.view_holder.LifecycleOwnerViewBindHolder

abstract class BasePagingWithPinHeaderRvAdapter<HeaderVB : ViewBinding, ItemVB : ViewBinding, DATA : Any>(
    private val headerVBInflate: Inflate<HeaderVB>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
) : PagingDataAdapter<DATA, LifecycleOwnerViewBindHolder>(diffCallback), IHeaderAdapterSetting {

    lateinit var context: Context

    val isContextInitialized get() = this::context.isInitialized

    abstract fun getViewBindingInflate(viewType: Int): Inflate<ItemVB>

    companion object {
        const val TYPE_HEADER = -1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) {
            TYPE_HEADER
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerViewBindHolder {
        if (!isContextInitialized) context = parent.context

        when (viewType) {
            TYPE_HEADER ->
                return HeaderViewHolder(
                    headerVBInflate.invoke(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    createHeaderHolder(binding as HeaderVB, this)
                }

            else -> {
                return ItemViewHolder(
                    getViewBindingInflate(viewType).invoke(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).apply {
                    createItemHolder(binding as ItemVB, this)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LifecycleOwnerViewBindHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            when (holder) {
                is BasePagingWithPinHeaderRvAdapter<*, *, *>.HeaderViewHolder -> {
                    bindHeader(holder.binding as HeaderVB, data, position)
                }

                is BasePagingWithPinHeaderRvAdapter<*, *, *>.ItemViewHolder -> {
                    bindItem(holder.binding as ItemVB, data, position)
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: LifecycleOwnerViewBindHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        val data = getItem(position)

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (data != null) {
                    when (holder) {
                        is BasePagingWithPinHeaderRvAdapter<*, *, *>.HeaderViewHolder -> {
                            partBindHeader(payload, holder.binding as HeaderVB, data, position)
                        }

                        is BasePagingWithPinHeaderRvAdapter<*, *, *>.ItemViewHolder -> {
                            partBindItem(payload, holder.binding as ItemVB, data, position)
                        }
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: LifecycleOwnerViewBindHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attachToWindow()
    }

    override fun onViewDetachedFromWindow(holder: LifecycleOwnerViewBindHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detachToWindow()
    }


    abstract fun bindHeader(binding: HeaderVB, item: DATA, position: Int)
    abstract fun partBindHeader(payload: Any, binding: HeaderVB, item: DATA, position: Int)
    abstract fun createHeaderHolder(binding: HeaderVB, viewHolder: RecyclerView.ViewHolder)

    abstract fun bindItem(binding: ItemVB, item: DATA, position: Int)
    abstract fun partBindItem(payload: Any, binding: ItemVB, item: DATA, position: Int)
    abstract fun createItemHolder(binding: ItemVB, viewHolder: RecyclerView.ViewHolder)


    inner class HeaderViewHolder(binding: ViewBinding) : LifecycleOwnerViewBindHolder(binding)


    inner class ItemViewHolder(binding: ViewBinding) : LifecycleOwnerViewBindHolder(binding)

}