package com.example.baseadapterslibrary.adapter.normal.checkbox

import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.model.IExpandableCheckBox
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder

/**
 * @param needResetToDefaultSelectAfterdloseExpand 是否需要在收起展開狀態之後重置回原本的checkbox邏輯
 */
abstract class ExpandableCheckBoxAdapter<VB : ViewBinding, CB : IExpandableCheckBox>(private val needResetToDefaultSelectAfterdloseExpand: Boolean) : CheckBoxAdapter<VB, CB>() {

    private val realSelectCheckBoxMap = mutableMapOf<Int, CB>()

    /**
     * 會根據isExpand的狀態來決定ＵＩ初始化
     */
    override fun onBindViewHolder(holder: BaseViewBindHolder, position: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        val item = dataList[adapterPosition]
        doWhenBindHolder(holder.binding as VB, item, adapterPosition, holder)
        onCheckStateExchange(selectCheckBoxMap.containsKey(position), holder.binding, item, position)
        onExpandChange(holder.binding, item.isExpand, item, position)
    }

    override fun onBindViewHolder(holder: BaseViewBindHolder, position: Int, payloads: MutableList<Any>) {
        val adapterPosition = holder.bindingAdapterPosition
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (payload is Boolean) {
                    onCheckStateExchange(
                        payload,
                        holder.binding as VB,
                        dataList[adapterPosition],
                        adapterPosition
                    )
                } else if (payload is IExpandableCheckBox) {
                    onExpandChange(holder.binding as VB, payload.isExpand, dataList[adapterPosition], adapterPosition)
                } else {
                    doWhenBindPayload(payload, holder.binding as VB, dataList[adapterPosition], adapterPosition, holder)
                }
            }
        }
    }

    fun changeExpandAllData(isExpand: Boolean) {
        when (isExpand) {
            true -> {
                openExpandAllData()
            }

            false -> {
                closeExpandAllData()
            }
        }
    }


    private fun openExpandAllData() {
        dataList.forEachIndexed { index, cb ->
            cb.isExpand = true
            notifyItemChanged(index, cb)
        }
    }

    private fun closeExpandAllData() {
        dataList.forEachIndexed { index, cb ->
            cb.isExpand = false
            if (needResetToDefaultSelectAfterdloseExpand) {
                val defaultSelectItem = realSelectCheckBoxMap[index]
                if (defaultSelectItem != null) {
                    clickCheckBox(defaultSelectItem, index)
                }
            }
            notifyItemChanged(index, cb)
        }
    }

    protected abstract fun onExpandChange(viewBinding: VB, isExpand: Boolean, cb: CB, adapterPosition: Int)

    /**
     * 組件默認點擊狀態
     */
    fun clickExpandItem(binding: VB, adapterPosition: Int) {

        val newState = !dataList[adapterPosition].isExpand

        dataList[adapterPosition].isExpand = newState

        onExpandChange(
            binding,
            newState,
            dataList[adapterPosition],
            adapterPosition
        )
    }

    /**
     * 指定任意位置組件更改為任意狀態
     */
    fun changeExpandItem(binding: VB, isExpand: Boolean, adapterPosition: Int) {

        dataList[adapterPosition].isExpand = isExpand

        onExpandChange(
            binding,
            isExpand,
            dataList[adapterPosition],
            adapterPosition
        )
    }

    override fun removeItem(position: Int) {
        realSelectCheckBoxMap.remove(position)
        super.removeItem(position)
    }
}