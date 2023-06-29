package com.example.baseadapterslibrary.adapter.normal


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.model.IListDataModifySetting
import com.example.baseadapterslibrary.model.NormalRvLoadState
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseRvAdapter<VB : ViewBinding, DATA> : RecyclerView.Adapter<BaseViewBindHolder>(), IListDataModifySetting<DATA> {

    lateinit var context: Context

    open var dataList: MutableList<DATA> = mutableListOf()

    val isContextInitialized get() = this::context.isInitialized

    protected var loadStateCallback: ((NormalRvLoadState) -> Unit)? = null

    protected var itemRemoveCallback: ((item: DATA?, position: Int) -> Unit)? = null

    protected var itemAddCallback: ((item: DATA) -> Unit)? = null

    protected var itemSetCallback: ((item: DATA, position: Int) -> Unit)? = null

    protected var itemRangeRemoveCallback: ((positionStart: Int, list: List<DATA>) -> Unit)? = null

    protected var itemRangeInsertCallback: ((positionStart: Int, list: List<DATA>) -> Unit)? = null

    protected var itemClickCallback: ((item: DATA, position: Int) -> Unit)? = null

    override fun setItemClickListener(callback: (item: DATA?, position: Int) -> Unit) {
        this.itemClickCallback = callback
    }

    override fun setItemRangeInsertListener(callback: (positionStart: Int, list: List<DATA>) -> Unit) {
        this.itemRangeInsertCallback = callback
    }

    override fun setItemRangeRemoveListener(callback: (positionStart: Int, list: List<DATA>) -> Unit) {
        this.itemRangeRemoveCallback = callback
    }

    override fun setAddItemListener(callback: (item: DATA) -> Unit) {
        this.itemAddCallback = callback
    }

    override fun setInsertItemListener(callback: (item: DATA, position: Int) -> Unit) {
        this.itemSetCallback = callback
    }

    override fun setRemoveItemListener(callback: (item: DATA?, position: Int) -> Unit) {
        this.itemRemoveCallback = callback
    }

    final override suspend fun updateDataSet(newDataSet: MutableList<DATA>) = withContext(Dispatchers.Main) {
        updateDataAction(newDataSet)
    }

    protected open suspend fun updateDataAction(newDataSet: MutableList<DATA>) {
        loadStateCallback?.invoke(NormalRvLoadState.NoData(false))

        val diff = getDiffWay(newDataSet)
        dataList.clear()
        dataList.addAll(newDataSet)

        if (dataList.isEmpty()) {
            loadStateCallback?.invoke(NormalRvLoadState.NoData(true))
        } else {
            loadStateCallback?.invoke(NormalRvLoadState.HaveData)
        }

        withContext(Dispatchers.Main) {
            diff.dispatchUpdatesTo(this@BaseRvAdapter)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewBindHolder {
        if (!isContextInitialized) context = parent.context

        return getViewHolder(parent, viewType).apply {
            doWhenCreateHolder(binding as VB, this)
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
                doWhenBindPayload(
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
        doWhenBindHolder(holder.binding as VB, dataList[adapterPosition], dataList.indexOf(dataList[adapterPosition]), holder)
    }

    override fun getItemCount() = dataList.size

    abstract fun getViewBindingInflate(viewType: Int): Inflate<VB>
    abstract fun doWhenCreateHolder(binding: VB, viewHolder: BaseViewBindHolder)
    abstract fun doWhenBindHolder(binding: VB, item: DATA, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder)
    abstract fun doWhenBindPayload(payload: Any, binding: VB, item: DATA, bindingAdapterPosition: Int, viewHolder: BaseViewBindHolder)

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
        this.loadStateCallback = listener
    }
}
