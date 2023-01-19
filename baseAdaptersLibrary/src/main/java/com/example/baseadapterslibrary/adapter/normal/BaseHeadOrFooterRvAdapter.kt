package com.example.baseadapterslibrary.adapter.normal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseHeadOrFooterRvAdapter<VB : ViewBinding, DATA> : BaseRvAdapter<VB, DATA>() {

    companion object {
        const val TYPE_HEADER = -1 //說明是帶有Header的

        const val TYPE_FOOTER = -2 //說明是帶有Footer的
    }

    abstract val type: HeaderOrFooterRVType

    abstract fun getHeaderView(parent: ViewGroup): ViewBinding?

    abstract fun getFooterView(parent: ViewGroup): ViewBinding?

    override suspend fun updateDataSet(newDataSet: MutableList<DATA>) = withContext(Dispatchers.Default) {
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
            dataList.clear()
            if (newDataSet.isNotEmpty()) setHeader()
            dataList.addAll(newDataSet)
            if (newDataSet.isNotEmpty()) setFooter()
            diff.dispatchUpdatesTo(this@BaseHeadOrFooterRvAdapter)
        }
    }

    private fun setFooter() {
        if (type == HeaderOrFooterRVType.HeaderAndFooter || type == HeaderOrFooterRVType.Footer) notifyItemInserted(itemCount - 1)
    }

    private fun setHeader() {
        if (type == HeaderOrFooterRVType.HeaderAndFooter || type == HeaderOrFooterRVType.Header) notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        if (!isContextInitialized) context = parent.context

        return when (viewType) {
            TYPE_HEADER -> {
                BaseBindHolder(getHeaderView(parent)!!)
            }
            TYPE_FOOTER -> {
                BaseBindHolder(getFooterView(parent)!!)
            }

            else -> BaseBindHolder(getViewBindingInflate(viewType).invoke(LayoutInflater.from(parent.context), parent, false)).apply {
                createHolder(binding as VB, this)
            }
        }

    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int, payloads: MutableList<Any>) {
        when (getItemViewType(position)) {
            TYPE_FOOTER -> return
            TYPE_HEADER -> return
            else -> {
                val adapterPosition = when (type) {
                    HeaderOrFooterRVType.Footer -> holder.bindingAdapterPosition
                    HeaderOrFooterRVType.Header -> holder.bindingAdapterPosition - 1
                    else -> holder.bindingAdapterPosition -1
                }


                if (payloads.isEmpty()) {
                    super.onBindViewHolder(holder, adapterPosition, payloads)
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
        }
    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {


        when (getItemViewType(holder.bindingAdapterPosition)) {
            TYPE_FOOTER -> return
            TYPE_HEADER -> return
            else -> {
                val adapterPosition = when (type) {
                    HeaderOrFooterRVType.Footer -> holder.bindingAdapterPosition
                    HeaderOrFooterRVType.Header -> holder.bindingAdapterPosition - 1
                    else -> holder.bindingAdapterPosition -1
                }

                bind(holder.binding as VB, dataList[adapterPosition], dataList.indexOf(dataList[adapterPosition]), holder)
            }
        }
    }

    @Override
    override fun getItemCount(): Int {
        return when (type) {
            HeaderOrFooterRVType.Header, HeaderOrFooterRVType.Footer -> {
                if (dataList.isNotEmpty()) dataList.size + 1 else 0
            }
            else -> {
                if (dataList.isNotEmpty()) dataList.size + 2 else 0
            }
        }
    }

    /**
     *重寫這個方法，很重要，是加入Header和Footer的關鍵，我們通過判斷item的型別，從而繫結不同的view
     **/
    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 && (type == HeaderOrFooterRVType.Header || type == HeaderOrFooterRVType.HeaderAndFooter) -> { //第一個item應該載入Header
                TYPE_HEADER
            }
            position == itemCount - 1 && (type == HeaderOrFooterRVType.Footer || type == HeaderOrFooterRVType.HeaderAndFooter) -> { //最後一個,應該載入Footer
                TYPE_FOOTER
            }
            else -> getNormalItemViewType(position) ?: super.getItemViewType(position)
        }
    }

    abstract fun getNormalItemViewType(position: Int): Int?
}

enum class HeaderOrFooterRVType {
    Header, Footer, HeaderAndFooter
}