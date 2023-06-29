package com.example.baseadapterslibrary.adapter.paging.checkbox

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.model.IPagingExpandableCheckBox
import com.example.baseadapterslibrary.view_holder.LifecycleOwnerViewBindHolder

abstract class ExpandableCheckBoxPagingAdapter<VB : ViewBinding, ECB : IPagingExpandableCheckBox, ID>(diffCallback: DiffUtil.ItemCallback<ECB>) : CheckBoxPagingAdapter<VB, ECB, ID>(diffCallback) {

    private val liveExpandDataMap = MutableLiveData<MutableMap<ID, ECB>>()

    private val expandDataMap = mutableMapOf<ID, ECB>()

    abstract fun onExpandChange(binding: VB, item: ECB, position: Int, isExpand: Boolean)

    private var expandStatus = ExpandStatus.Normal

    protected open fun clickToChangeExpand(binding: VB, data: ECB, viewHolder: LifecycleOwnerViewBindHolder) {
        data.apply {
            isExpand = !isExpand
            if (isExpand) {
                onDataExpand(data)
            } else {
                onDataFold(data)
            }
        }
        expandStatus = ExpandStatus.Normal
    }

    private fun onDataExpand(data: ECB) {
        val uuid = getDataUUid(data)
        expandDataMap[uuid] = data
        liveExpandDataMap.value = expandDataMap
    }

    private fun onDataFold(data: ECB) {
        val uuid = getDataUUid(data)
        val removeData = expandDataMap.remove(uuid)
        if (removeData != null) {
            liveExpandDataMap.value = expandDataMap
        }
    }

    /**
     * expand all items.
     */
    fun expandAll() {
        for (i in 0 until itemCount) {
            if (!isItemExpanded(i)) getItem(i)?.let {
                expandDataMap[getDataUUid(it)] = it
                it.isExpand = true
            }
        }
        expandStatus = ExpandStatus.ExpandAll
        liveExpandDataMap.value = expandDataMap
    }

    fun foldAll() {
        for (i in 0 until itemCount) {
            getItem(i)?.let {
                it.isExpand = false
            }
        }
        expandDataMap.clear()
        expandStatus = ExpandStatus.FoldAll
        liveExpandDataMap.value = expandDataMap
    }

    override fun doWhenInit(data: ECB, position: Int, holder: LifecycleOwnerViewBindHolder) {
        super.doWhenInit(data, position, holder)
        doExpandLogicWhenInit(data)
    }

    private fun doExpandLogicWhenInit(data: ECB) {
        if (expandStatus == ExpandStatus.ExpandAll) {
            data.isExpand = true
        } else if (expandStatus == ExpandStatus.FoldAll) {
            data.isExpand = false
        }

        if (data.isExpand) {
            onDataExpand(data)
        } else {
            onDataFold(data)
        }
    }

    override fun doObserveDataChange(holder: LifecycleOwnerViewBindHolder) {
        super.doObserveDataChange(holder)
        observeExpandStatus(holder)
    }

    /**
     * Indicate if an item is already selected.
     */
    private fun isItemExpanded(position: Int): Boolean {
        val data = getItem(position) ?: return false
        return expandDataMap.contains(getDataUUid(data))
    }

    private fun observeExpandStatus(holder: LifecycleOwnerViewBindHolder) {
        //觀察是否展開，以 view holder 做為生命週期
        liveExpandDataMap.observe(holder) {
            val newPosition = holder.adapterPosition
            if (newPosition == RecyclerView.NO_POSITION) return@observe
            val selectByMapData = getItem(newPosition)
            if (selectByMapData != null) {
                val isExpand = isItemExpanded(newPosition)
                onExpandChange(holder.binding as VB, selectByMapData, newPosition, isExpand)
            }
        }
    }

    private enum class ExpandStatus {
        Normal, ExpandAll, FoldAll
    }
}