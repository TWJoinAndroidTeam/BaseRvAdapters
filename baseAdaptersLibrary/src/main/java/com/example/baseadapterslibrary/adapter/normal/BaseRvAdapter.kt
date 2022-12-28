package com.example.baseadapterslibrary.adapter.normal


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseRvAdapter<VB : ViewBinding, DATA>(
    private val inflate: Inflate<VB>,
) : RecyclerView.Adapter<BaseRvAdapter.BaseBindHolder>() {

    lateinit var context: Context

    open var dataList: MutableList<DATA> = mutableListOf()

    open suspend fun updateDataSet(newDataSet: MutableList<DATA>) = withContext(Dispatchers.Main) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return dataList.size
            }

            override fun getNewListSize(): Int {
                return newDataSet.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItemPosition == newItemPosition
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return dataList[oldItemPosition] == newDataSet[newItemPosition]
            }
        })

        withContext(Dispatchers.Main) {
            dataList = newDataSet
            diff.dispatchUpdatesTo(this@BaseRvAdapter)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        if (!::context.isInitialized) context = parent.context

        return BaseBindHolder(
            inflate.invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(
        holder: BaseBindHolder,
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

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
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

    abstract fun bind(binding: VB, item: DATA, bindingAdapterPosition: Int, viewHolder: BaseBindHolder)
    abstract fun partBind(payload: Any, binding: VB, item: DATA, bindingAdapterPosition: Int, viewHolder: BaseBindHolder)

    class BaseBindHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
}
