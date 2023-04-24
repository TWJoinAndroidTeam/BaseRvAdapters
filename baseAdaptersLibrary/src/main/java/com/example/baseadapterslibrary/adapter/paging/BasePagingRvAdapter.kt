package com.example.baseadapterslibrary.adapter.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.view_holder.LifecycleOwnerViewBindHolder


abstract class BasePagingRvAdapter<VB : ViewBinding, DATA : Any>(
    diffCallback: DiffUtil.ItemCallback<DATA>,
) : PagingDataAdapter<DATA, LifecycleOwnerViewBindHolder>(diffCallback) {

    lateinit var context: Context

    val isContextInitialized get() = this::context.isInitialized

    protected var onItemClickListener: ((DATA, position: Int) -> Unit)? = null

    abstract fun getViewBindingInflate(viewType: Int): Inflate<VB>

    @JvmName("setOnItemClickListener1")
    fun setOnItemClickListener(listener: (DATA, position: Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerViewBindHolder {
        if (!isContextInitialized) context = parent.context
        return getViewHolder(parent, viewType).apply {
            doWhenCreateViewHolder(binding as VB, this)
        }
    }

    protected open fun getViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerViewBindHolder {
        return LifecycleOwnerViewBindHolder(
            getViewBindingInflate(viewType).invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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
                    doWhenBindPayload(
                        payload,
                        holder.binding as VB,
                        data,
                        position
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LifecycleOwnerViewBindHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            doWhenBindHolder(holder.binding as VB, data, position)
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

    abstract fun doWhenCreateViewHolder(binding: VB, viewHolder: LifecycleOwnerViewBindHolder)

    abstract fun doWhenBindHolder(binding: VB, item: DATA, position: Int)

    abstract fun doWhenBindPayload(payload: Any, binding: VB, item: DATA, position: Int)


}