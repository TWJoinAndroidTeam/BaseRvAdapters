package com.example.baseadapterslibrary.adapter.normal.checkbox

import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.module.ExpandableCheckBoxModel
import com.example.baseadapterslibrary.module.ExpandableICheckBox

abstract class ExpandableCheckBoxAdapter<VB : ViewBinding, CB : ExpandableICheckBox>(private val needRememberDefaultUtilChange: Boolean) : CheckBoxAdapter<VB, CB>() {

    private var isExpand = false

    var realSelectCheckBoxMap = mutableMapOf<Int, CB>()

    private var realDataList: MutableList<CB> = mutableListOf()

    /**
     * 會根據isExpand的狀態來決定ＵＩ初始化
     */
    override fun onBindViewHolder(holder: CheckBoxBindHolder, position: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        val item = checkBoxList[adapterPosition]
        bind(holder.binding as VB, item, adapterPosition, holder)
        onCheckStateExchange(selectCheckBoxMap.containsKey(position), holder.binding, item, position)
        onExpandChange(holder.binding, isExpand, item, position)
    }

    override fun onBindViewHolder(holder: CheckBoxBindHolder, position: Int, payloads: MutableList<Any>) {
        val adapterPosition = holder.bindingAdapterPosition
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if (payload is Boolean) {
                    onCheckStateExchange(
                        payload,
                        holder.binding as VB,
                        checkBoxList[adapterPosition],
                        adapterPosition
                    )
                } else if (payload is ExpandableCheckBoxModel<*>) {
                    onExpandChange(holder.binding as VB, payload.isExpand, checkBoxList[adapterPosition], adapterPosition)
                }
            }
        }
    }

    fun changeExpandAllData(isExpand: Boolean) {
        this.isExpand = isExpand
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
        checkBoxList.forEachIndexed { index, cb ->
            cb.isExpand = true
            notifyItemChanged(index, cb)
        }
    }

    private fun closeExpandAllData() {
        checkBoxList.forEachIndexed { index, cb ->
            cb.isExpand = false
            if (needRememberDefaultUtilChange) {
                val defaultSelectItem = realSelectCheckBoxMap[index]
                if (defaultSelectItem != null) {
                    onCheckBoxClick(defaultSelectItem, index)
                }
            }
            notifyItemChanged(index, cb)

        }
    }

    abstract fun onExpandChange(viewBinding: VB, isExpand: Boolean, cb: CB, adapterPosition: Int)


    override suspend fun setData(checkBoxList: MutableList<CB>) {

        realDataList.clear()
        realDataList.addAll(checkBoxList)

        if (checkBoxList.isNotEmpty()) {
            selectCheckBoxMap.clear()

            for (i in checkBoxList.indices) {
                if (checkBoxList[i].isCheck) {
                    selectCheckBoxMap[i] = checkBoxList[i]
                    realSelectCheckBoxMap[i] = checkBoxList[i]
                }
            }
            updateDataSet(checkBoxList)
        }
    }

    override fun removeItem(position: Int) {
        realDataList.removeAt(position)
        realSelectCheckBoxMap.remove(position)
        super.removeItem(position)
    }
}