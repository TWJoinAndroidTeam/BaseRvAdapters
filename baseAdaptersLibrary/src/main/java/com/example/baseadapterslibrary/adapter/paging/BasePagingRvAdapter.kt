package com.example.baseadapterslibrary.adapter.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate


abstract class BasePagingRvAdapter<VB : ViewBinding, DATA : Any>(
    private val inflate: Inflate<VB>, diffCallback: DiffUtil.ItemCallback<DATA>,
) : PagingDataAdapter<DATA, LifecycleOwnerBindHolder>(diffCallback) {

    lateinit var context: Context

    val isContextInitialized get() = this::context.isInitialized

    internal var onItemClickListener: ((DATA, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (DATA, position: Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerBindHolder {
        if (!isContextInitialized) context = parent.context
        return getViewHolder(parent, viewType).apply {
            createHolder(binding as VB, this)
        }
    }

    protected open fun getViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerBindHolder {
        return LifecycleOwnerBindHolder(
            inflate.invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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
                    partBind(
                        payload,
                        holder.binding as VB,
                        data,
                        position
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LifecycleOwnerBindHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            bind(holder.binding as VB, data, position)
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

    abstract fun bind(binding: VB, item: DATA, position: Int)
    abstract fun partBind(payload: Any, binding: VB, item: DATA, position: Int)
    abstract fun createHolder(binding: VB, viewHolder: RecyclerView.ViewHolder)

}

/**
 *繼承 LifecycleOwner ，讓view Holder 也能持有生命週期
 *
 *@property lifecycleRegistry view holder 生命週期管理者
 *@property paused 控制生命週期暫離與否
 */
open class LifecycleOwnerBindHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    private var paused: Boolean = false

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun lifecycleCreate() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun attachToWindow() {
        if (paused) {
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
            paused = false
        } else {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    fun detachToWindow() {
        paused = true
    }

}