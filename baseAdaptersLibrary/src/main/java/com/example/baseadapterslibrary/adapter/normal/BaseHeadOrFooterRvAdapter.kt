package com.example.baseadapterslibrary.adapter.normal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseHeadOrFooterRvAdapter<VB : ViewBinding, DATA>(private val inflate: Inflate<VB>) : BaseRvAdapter<VB, DATA>(inflate) {

    companion object {
        const val TYPE_HEADER = 1 //說明是帶有Header的

        const val TYPE_FOOTER = 2 //說明是帶有Footer的

        const val TYPE_BOTH_HEADER_AND_FOOTER = 3 //說明是帶有header和Footer的

        const val TYPE_NORMAL = 4 //說明是不帶有header和footer的
    }

    //HeaderView和FooterView的get和set函式
    var headerView: View? = null
    var footerView: View? = null

    abstract val type: Int

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
        if (type == TYPE_BOTH_HEADER_AND_FOOTER || type == TYPE_FOOTER) notifyItemInserted(itemCount - 1)
    }

    private fun setHeader() {
        if (type == TYPE_BOTH_HEADER_AND_FOOTER || type == TYPE_FOOTER) notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                BaseBindHolder(getHeaderView(parent)!!)
            }
            TYPE_FOOTER -> {
                BaseBindHolder(getFooterView(parent)!!)
            }

            else -> BaseBindHolder(inflate.invoke(LayoutInflater.from(parent.context), parent, false)).apply {
                createHolder(binding as VB, this)
            }
        }

    }

    override fun onBindViewHolder(holder: BaseBindHolder, position: Int, payloads: MutableList<Any>) {
        val adapterPosition = when (type) {
            TYPE_BOTH_HEADER_AND_FOOTER, TYPE_HEADER -> holder.bindingAdapterPosition - 1
            else -> holder.bindingAdapterPosition
        }
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
        val adapterPosition = when (type) {
            TYPE_BOTH_HEADER_AND_FOOTER, TYPE_HEADER -> holder.bindingAdapterPosition - 1
            else -> holder.bindingAdapterPosition
        }

        if (getItemViewType(position) == TYPE_NORMAL) {
            bind(holder.binding as VB, dataList[adapterPosition], dataList.indexOf(dataList[adapterPosition]), holder)
        } else return
    }

    @Override
    override fun getItemCount(): Int {
        return when (type) {
            TYPE_NORMAL -> {
                dataList.size
            }
            TYPE_HEADER, TYPE_FOOTER -> {
                if (dataList.isNotEmpty()) dataList.size + 1 else dataList.size
            }
            else -> {
                if (dataList.isNotEmpty()) dataList.size + 2 else dataList.size
            }
        }
    }

    /**
     *重寫這個方法，很重要，是加入Header和Footer的關鍵，我們通過判斷item的型別，從而繫結不同的view
     **/
    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 && (type == TYPE_HEADER || type == TYPE_BOTH_HEADER_AND_FOOTER) -> { //第一個item應該載入Header
                TYPE_HEADER
            }
            position == itemCount - 1 && (type == TYPE_FOOTER || type == TYPE_BOTH_HEADER_AND_FOOTER) -> { //最後一個,應該載入Footer
                TYPE_FOOTER
            }
            else -> TYPE_NORMAL
        }
    }
}