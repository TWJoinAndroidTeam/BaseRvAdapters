package com.example.baseadapterslibrary.adapter.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.module.IHeaderAdapterSetting

abstract class BasePagingWithPinHeaderRvAdapter<HeaderVB : ViewBinding, ItemVB : ViewBinding, DATA : Any>(
    private val headerVBInflate: Inflate<HeaderVB>,
    private val itemVBInflate: Inflate<ItemVB>,
    diffCallback: DiffUtil.ItemCallback<DATA>,
) : PagingDataAdapter<DATA, LifecycleOwnerBindHolder>(diffCallback), IHeaderAdapterSetting {

    lateinit var context: Context


    companion object {
        const val TYPE_HEADER = 0

        const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerBindHolder {
        if (!::context.isInitialized) context = parent.context

        when (viewType) {
            TYPE_HEADER ->
                return HeaderViewHolder(
                    headerVBInflate.invoke(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            else -> {
                return ItemViewHolder(
                    itemVBInflate.invoke(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: LifecycleOwnerBindHolder, position: Int) {
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
        holder: LifecycleOwnerBindHolder,
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

    override fun onViewAttachedToWindow(holder: LifecycleOwnerBindHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attachToWindow()
    }

    override fun onViewDetachedFromWindow(holder: LifecycleOwnerBindHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detachToWindow()
    }


    abstract fun bindHeader(binding: HeaderVB, item: DATA, position: Int)
    abstract fun partBindHeader(payload: Any, binding: HeaderVB, item: DATA, position: Int)
    abstract fun bindItem(binding: ItemVB, item: DATA, position: Int)
    abstract fun partBindItem(payload: Any, binding: ItemVB, item: DATA, position: Int)


    inner class HeaderViewHolder(binding: ViewBinding) : LifecycleOwnerBindHolder(binding)


    inner class ItemViewHolder(binding: ViewBinding) : LifecycleOwnerBindHolder(binding)

}