package com.example.baseadapterslibrary.adapter.normal


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.module.NormalRvLoadState
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseRvAdapter<VB : ViewBinding, DATA> : RecyclerView.Adapter<BaseViewBindHolder>() {

    lateinit var context: Context

    open var dataList: MutableList<DATA> = mutableListOf()

    val isContextInitialized get() = this::context.isInitialized

    abstract fun getViewBindingInflate(viewType: Int): Inflate<VB>

    private var loadStateListener: ((NormalRvLoadState) -> Unit)? = null

    open suspend fun updateDataSet(newDataSet: MutableList<DATA>) = withContext(Dispatchers.Main) {

        loadStateListener?.invoke(NormalRvLoadState.NoData(false))

        val diff = getDiffWay(newDataSet)
        dataList.clear()
        dataList.addAll(newDataSet)

        if (dataList.isEmpty()) {
            loadStateListener?.invoke(NormalRvLoadState.NoData(true))
        } else {
            loadStateListener?.invoke(NormalRvLoadState.HaveData)
        }

        withContext(Dispatchers.Main) {
            diff.dispatchUpdatesTo(this@BaseRvAdapter)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewBindHolder {
        if (!isContextInitialized) context = parent.context

        return getViewHolder(parent, viewType).apply {
            createHolder(binding as VB, this)
        }
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

    override fun onBindViewHolder(
        holder: BaseViewBindHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        val adapterPosition = holder.bindingAdapterPosition
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                partBind(
                    payload,
                    holder.binding as VB,
                    dataList[adapterPosition],
                    dataList.indexOf(dataList[adapterPosition]),
                    holder
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewBindHolder, position: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        bind(holder.binding as VB, dataList[adapterPosition], dataList.indexOf(dataList[adapterPosition]), holder)
    }

    override fun getItemCount() = dataList.size

    fun removeItem(item: DATA, position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size)
    }

    fun addData(data: DATA) {
        this.dataList.add(data)
        notifyItemInserted(dataList.indices.last)
//        notifyItemRangeChanged(dataList.indices.last, dataList.size)
    }


    abstract fun createHolder(binding: VB, viewHolder: RecyclerView.ViewHolder)
    abstract fun bind(binding: VB, item: DATA, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder)
    abstract fun partBind(payload: Any, binding: VB, item: DATA, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder)


    open fun getDiffWay(newDataSet: MutableList<DATA>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return dataList.size
            }

            override fun getNewListSize(): Int {
                return newDataSet.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldData = newDataSet.getOrNull(oldItemPosition)
                val newData = dataList.getOrNull(newItemPosition)

                return when {
                    oldData == newData || newData == null -> false
                    else -> oldData == newData
                }
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

                val oldData = newDataSet.getOrNull(oldItemPosition)
                val newData = dataList.getOrNull(newItemPosition)

                return when {
                    oldData == newData || newData == null -> false
                    else -> oldData == newData
                }
            }
        })
    }

    fun setLoadStateAdapterListener(listener: (NormalRvLoadState) -> Unit) {
        this.loadStateListener = listener
    }
}
