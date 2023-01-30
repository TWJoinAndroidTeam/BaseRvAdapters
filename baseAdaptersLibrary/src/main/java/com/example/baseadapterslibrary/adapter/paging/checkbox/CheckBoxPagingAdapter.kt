package com.example.baseadapterslibrary.adapter.paging.checkbox

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.module.ChooserMode
import com.example.baseadapterslibrary.module.ICheckBoxSetting
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.baseAdaptersLibrary.module.IPagingCheckBox
import com.example.baseadapterslibrary.adapter.paging.BasePagingRvAdapter
import com.example.baseadapterslibrary.adapter.paging.LifecycleOwnerBindHolder

abstract class CheckBoxPagingAdapter<VB : ViewBinding, CB : IPagingCheckBox>(
    diffCallback: DiffUtil.ItemCallback<CB>,
) : BasePagingRvAdapter<VB, CB>(diffCallback), ICheckBoxSetting<CB> {

    abstract val chooserMode: ChooserMode

    var liveIsExpand: MutableLiveData<Boolean> = MutableLiveData(false)

    //由於 paging 只能撈部分資料，所以需要變數讓之後資料onBind時知道被選取
    private var isSelectAll = false

    private val liveSelectCheckBoxMap = MutableLiveData<MutableMap<Int, CB>>()

    private var selectCheckBoxMap = mutableMapOf<Int, CB>()

    private var onCheckBoxClickListener: ((CB, position: Int) -> Unit)? = null

    fun setOnCheckBoxClickListener(listener: (CB, position: Int) -> Unit) {
        onCheckBoxClickListener = listener
    }

    protected var onCheckBoxChangeSelectAllListener: ((MutableMap<Int, CB>, isSelectAll: Boolean) -> Unit)? = null

    @JvmName("setOnCheckBoxChangeSelectAllListener1")
    fun setOnCheckBoxChangeSelectAllListener(listener: (MutableMap<Int, CB>, isSelectAll: Boolean) -> Unit) {
        onCheckBoxChangeSelectAllListener = listener
    }

    /**
     * 改變開展狀態
     */
    fun changeExpand() {
        val expandStatus = liveIsExpand.value
        liveIsExpand.postValue(!expandStatus!!)
    }

    /**
     * 改變選擇狀態時，只需使用這個方法
     */
    open fun changeSelect(position: Int) {
        val isSelect = isItemSelected(position)

        val checkBoxModel = getItem(position)

        if (checkBoxModel != null) {
            when (chooserMode) {
                is ChooserMode.MultipleResponse -> {
                    //取消選擇
                    if (isSelect && checkBoxModel.isInit) {
                        removeItemFromSelection(position)
                        checkBoxModel.isCheck = false
                        onCheckBoxClickListener?.invoke(checkBoxModel, position)
                    } else {
                        //選擇
                        addItemToSelection(position, checkBoxModel)
                        checkBoxModel.isCheck = true
                        onCheckBoxClickListener?.invoke(checkBoxModel, position)
                    }
                }

                is ChooserMode.SingleChoice -> {
                    //取消選擇
                    if (isSelect && checkBoxModel.isInit) {
                        removeItemFromSelection(position)
                        checkBoxModel.isCheck = false
                        onCheckBoxClickListener?.invoke(checkBoxModel, position)
                    } else {
                        //選擇，但因為是單選，所以選擇前會先清空資料
                        clearAll()
                        addItemToSelection(position, checkBoxModel)
                        checkBoxModel.isCheck = true
                        onCheckBoxClickListener?.invoke(checkBoxModel, position)
                    }
                }
            }
        }
    }

    /**
     * Add an item it the selection.
     */
    private fun addItemToSelection(position: Int, checkBoxModel: CB) {
        selectCheckBoxMap[position] = checkBoxModel
        liveSelectCheckBoxMap.value = selectCheckBoxMap
    }

    /**
     * Remove an item to the selection.
     */
    private fun removeItemFromSelection(position: Int) {
        selectCheckBoxMap.remove(position)
        liveSelectCheckBoxMap.value = selectCheckBoxMap
    }

    /**
     * Indicate if an item is already selected.
     */
    private fun isItemSelected(position: Int) = selectCheckBoxMap.contains(position)


    /**
     * Select all items.
     */
    override fun selectAll() {
        isSelectAll = true
        for (i in 0 until itemCount) {
            if (!isItemSelected(i)) getItem(i)?.let {
                selectCheckBoxMap[i] = it
                it.isCheck = true
            }
        }
        liveSelectCheckBoxMap.value = selectCheckBoxMap
        onCheckBoxChangeSelectAllListener?.invoke(selectCheckBoxMap, true)
    }

    /**
     * 單純清除資料，不改變資料原始型態
     */
    fun resetSelect() {
        selectCheckBoxMap.clear()
        liveSelectCheckBoxMap.value = selectCheckBoxMap
    }

    override fun clearAll() {
        isSelectAll = false
        for (i in 0 until itemCount) {
            getItem(i)?.let {
                it.isCheck = false
            }
        }
        resetSelect()
        onCheckBoxChangeSelectAllListener?.invoke(selectCheckBoxMap, false)
    }

    override fun reverseSelect() {

    }

    override fun getSelectDataPosition(): List<Int> {
        return selectCheckBoxMap.keys.toList()
    }

    override fun getSelectDataList(): MutableList<CB> {
        val items = mutableListOf<CB>()
        for (position in selectCheckBoxMap.keys) {
            val data = getItem(position)
            if (data != null) {
                items.add(data)
            }
        }
        return items
    }

    override fun getSelectDataMap(): MutableMap<Int, CB> {
        return selectCheckBoxMap
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerBindHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.lifecycleCreate()
        return holder
    }

    override fun onBindViewHolder(holder: LifecycleOwnerBindHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val data = getItem(position)

        if (isSelectAll) {
            data?.isCheck = true
        }

        if (data?.isInit == false) {
            if (data.isCheck) {
                selectCheckBoxMap[position] = data
            } else {
                removeItemFromSelection(position)
            }
            onSelectChange(holder.binding as VB, data, position, data.isCheck)
        }

        //觀察是否被選取，以 view holder 做為生命週期
        liveSelectCheckBoxMap.observe(holder, Observer {
            if (data != null) {
                val isSelect = isItemSelected(position)
                onSelectChange(holder.binding as VB, data, position, isSelect)
            }
        })

        //觀察是否展開,，以 view holder 做為生命週期
        liveIsExpand.observe(holder, Observer {
            if (data != null) {
                onExpandChange(holder.binding as VB, data, position, it)
            }
        })

        //最後改變init值，代表處理過bind，剩下的資料更換邏輯就交給live data
        data?.isInit = true
    }


    abstract fun onExpandChange(binding: VB, item: CB, position: Int, isExpand: Boolean)

    abstract fun onSelectChange(binding: VB, item: CB, position: Int, isSelect: Boolean)
}