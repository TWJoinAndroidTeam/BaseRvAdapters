package com.example.baseadapterslibrary.adapter.normal.checkbox


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.BaseRvAdapter
import com.example.baseadapterslibrary.extension.changeCheck
import com.example.baseadapterslibrary.model.ICheckBox
import com.example.baseadapterslibrary.model.ChooserMode
import com.example.baseadapterslibrary.model.ICheckBoxSetting
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

private typealias SortListIndex = Int

private typealias RealDataPosition = Int

abstract class CheckBoxAdapter<VB : ViewBinding, CB : ICheckBox> : BaseRvAdapter<VB, CB>(), ICheckBoxSetting<CB> {

    abstract val chooserMode: ChooserMode

    protected var selectCheckBoxMap = mutableMapOf<Int, CB>()

    protected var selectCheckBoxMultiHaveSortList = mutableListOf<Pair<RealDataPosition, CB>>()

    protected var selectCheckBoxMultiHaveSortMap = mutableMapOf<RealDataPosition, SortListIndex>()

    override suspend fun updateDataSet(newDataSet: MutableList<CB>) {


            selectCheckBoxMap.clear()
            selectCheckBoxMultiHaveSortList.clear()
            selectCheckBoxMultiHaveSortMap.clear()

            updateDataAction(newDataSet)

            for (i in newDataSet.indices) {

                val data = dataList[i]

                modifyData(i, data)
            }

    }

    protected open fun modifyData(position: Int, data: CB) {
        if (data.isCheck) addSelectItem(position) else removeSelectItem(position)
    }

    override fun onBindViewHolder(holder: BaseViewBindHolder, position: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        val item = dataList[adapterPosition]
        doWhenBindHolder(holder.binding as VB, item, adapterPosition, holder)
        onCheckStateExchange(selectCheckBoxMap.containsKey(position), holder.binding, item, position)
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
                if (payload is Boolean) {
                    onCheckStateExchange(
                        payload,
                        holder.binding as VB,
                        dataList[adapterPosition],
                        adapterPosition
                    )
                } else doWhenBindPayload(
                    payload,
                    holder.binding as VB,
                    dataList[adapterPosition],
                    adapterPosition,
                    holder
                )
            }
        }
    }

    /**
    點擊checkbox
     */
    fun clickCheckBox(cb: CB, position: Int) {

        if (chooserMode is ChooserMode.SingleChoice) {
            if (!(chooserMode as ChooserMode.SingleChoice).canRemoveSelect && cb.isCheck) return
        } else if (!cb.isCheck && chooserMode is ChooserMode.MultipleResponse && selectCheckBoxMultiHaveSortList.size >= ((chooserMode as? ChooserMode.MultipleResponse)?.selectLimitOption?.first?.coerceAtLeast(
                1
            ) ?: Int.MAX_VALUE)
        ) {
            if ((chooserMode as? ChooserMode.MultipleResponse)?.selectLimitOption?.second != true) return
            val firstSelectCB = selectCheckBoxMultiHaveSortList.first()
            if (firstSelectCB != cb) {
                firstSelectCB.second.changeCheck()
                setClickLogic(false, firstSelectCB.first)
            }
        }

        cb.changeCheck()

        val isSelect = cb.isCheck

        setClickLogic(isSelect, position)

        onItemClickCallback?.invoke(cb, position)
    }

    private fun setClickLogic(isSelect: Boolean, position: Int) {
        notifyItemChanged(position, isSelect)
    }

    /**
    全選
     */
    override fun selectAll() {
        dataList.forEachIndexed { index, cb ->
            cb.isCheck = true
            selectCheckBoxMap[index] = cb
        }
        notifyDataSetChanged()
    }

    /**
    清除
     */
    override fun clearAll() {
        dataList.forEachIndexed { index, cb ->
            cb.isCheck = false
        }
        selectCheckBoxMap.clear()
        notifyDataSetChanged()
    }

    /**
    反選
     */
    override fun reverseSelect() {
        dataList.forEachIndexed { index, cb ->
            cb.changeCheck()
            if (cb.isCheck) {
                selectCheckBoxMap[index] = cb
            } else {
                selectCheckBoxMap.remove(index)
            }
        }
        notifyDataSetChanged()
    }

    /**
    取得所有被選擇項目的陣列位置
     */
    override fun getSelectDataPosition(): MutableList<Int> {
        return selectCheckBoxMap.keys.toMutableList()
    }

    /**
    取得所有被選擇項目的資料陣列
     */
    override fun getSelectDataList(): MutableList<CB> {
        return selectCheckBoxMap.values.toMutableList()
    }

    /**
    取得所有被選擇項目的資料 Map
     */
    override fun getSelectDataMap(): MutableMap<Int, CB> {
        return selectCheckBoxMap.toMutableMap()
    }

    protected abstract fun onCheckStateExchange(isCheck: Boolean, binding: VB, checkBox: CB, position: Int)

    private fun addSelectItem(position: Int) {

        val selectCB = dataList[position]

        when (chooserMode) {
            is ChooserMode.MultipleResponse -> {

                selectCheckBoxMap[position] = selectCB
                if ((chooserMode as ChooserMode.MultipleResponse).selectLimitOption != null) {
                    selectCheckBoxMultiHaveSortList.add(Pair(position, selectCB))
                    selectCheckBoxMultiHaveSortMap[position] = selectCheckBoxMultiHaveSortList.lastIndex

                }
            }

            is ChooserMode.SingleChoice -> {

                selectCheckBoxMap.forEach {
                    if (it.value.isCheck && position != it.key) {
                        dataList[it.key].changeCheck()
                        removeSelectItem(it.key)
                        notifyItemChanged(it.key, dataList[it.key].isCheck)
                    }
                }
                selectCheckBoxMap[position] = selectCB
                dataList[position].isCheck = true
            }
        }

        dataList[position].isCheck = true
    }

    private fun removeSelectItem(position: Int) {
        when (chooserMode) {
            is ChooserMode.MultipleResponse -> {

                selectCheckBoxMap.remove(position)
                val index = selectCheckBoxMultiHaveSortMap[position]

                if (index != null) {
                    selectCheckBoxMultiHaveSortList.removeAt(index)

                    for (i in selectCheckBoxMultiHaveSortList.indices) {
                        selectCheckBoxMultiHaveSortMap[selectCheckBoxMultiHaveSortList[i].first] = i
                    }
                }
            }

            is ChooserMode.SingleChoice -> {
                selectCheckBoxMap.remove(position)
            }
        }
        dataList[position].isCheck = false
    }
}
