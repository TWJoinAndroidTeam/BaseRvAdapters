package com.example.baseadapterslibrary.adapter.paging.checkbox

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.paging.BasePagingRvAdapter
import com.example.baseadapterslibrary.model.ChooserMode
import com.example.baseadapterslibrary.model.ICheckBoxSetting
import com.example.baseadapterslibrary.model.IPagingCheckBox
import com.example.baseadapterslibrary.view_holder.LifecycleOwnerViewBindHolder

abstract class CheckBoxPagingAdapter<VB : ViewBinding, CB : IPagingCheckBox, ID>(diffCallback: DiffUtil.ItemCallback<CB>) : BasePagingRvAdapter<VB, CB>(diffCallback) {

    abstract val chooserMode: ChooserMode

    private val liveSelectCheckBoxMap = MutableLiveData<MutableMap<ID, CB>>()

    private var selectCheckBoxMap = mutableMapOf<ID, CB>()

    private var onCheckBoxClickListener: ((CB, position: Int) -> Unit)? = null

    protected abstract fun getDataUUid(data: CB): ID

    private var checkStatus = CheckBoxStatus.Normal

    fun setOnCheckBoxClickListener(listener: (CB, position: Int) -> Unit) {
        onCheckBoxClickListener = listener
    }

    protected var onCheckBoxChangeSelectAllListener: ((MutableMap<ID, CB>, isSelectAll: Boolean) -> Unit)? = null

    @JvmName("setOnCheckBoxChangeSelectAllListener1")
    fun setOnCheckBoxChangeSelectAllListener(listener: (MutableMap<ID, CB>, isSelectAll: Boolean) -> Unit) {
        onCheckBoxChangeSelectAllListener = listener
    }

    /**
     * 改變選擇狀態時，只需使用這個方法
     */
    open fun clickCheckBox(position: Int) {
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

            checkStatus = CheckBoxStatus.Normal
        }
    }

    /**
     * @param position Int
     * @param checkBoxModel CB
     * @return Boolean 是否加入成功
     */
    private fun addItemToSelection(position: Int, checkBoxModel: CB): Boolean {
        val uuid = getDataUUid(getItem(position) ?: return false)
        selectCheckBoxMap[uuid] = checkBoxModel
        liveSelectCheckBoxMap.value = selectCheckBoxMap
        return true
    }

    /**
     * Remove an item to the selection.
     */
    private fun removeItemFromSelection(position: Int): Boolean {
        val uuid = getDataUUid(getItem(position) ?: return false)
        selectCheckBoxMap.remove(uuid)
        liveSelectCheckBoxMap.value = selectCheckBoxMap
        return true
    }

    /**
     * Indicate if an item is already selected.
     */
    private fun isItemSelected(position: Int): Boolean {
        val data = getItem(position) ?: return false
        return selectCheckBoxMap.contains(getDataUUid(data))
    }


    /**
     * Select all items.
     */
    fun selectAll() {
        for (i in 0 until itemCount) {
            if (!isItemSelected(i)) getItem(i)?.let {
                selectCheckBoxMap[getDataUUid(it)] = it
                it.isCheck = true
            }
        }
        checkStatus = CheckBoxStatus.CheckAll
        liveSelectCheckBoxMap.value = selectCheckBoxMap
        onCheckBoxChangeSelectAllListener?.invoke(selectCheckBoxMap, true)
    }

    fun clearAll() {
        for (i in 0 until itemCount) {
            getItem(i)?.let {
                it.isCheck = false
            }
        }
        selectCheckBoxMap.clear()
        liveSelectCheckBoxMap.value = selectCheckBoxMap
        checkStatus = CheckBoxStatus.UncheckAll
        onCheckBoxChangeSelectAllListener?.invoke(selectCheckBoxMap, false)
    }


    fun getSelectDataList(): MutableList<CB> {
        val items = mutableListOf<CB>()
        items.addAll(selectCheckBoxMap.values.map { it })
        return items
    }

    fun getSelectDataMap(): MutableMap<ID, CB> {
        return selectCheckBoxMap
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleOwnerViewBindHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.lifecycleCreate()

        return holder
    }

    private fun doCheckLogicWhenInit(data: CB, position: Int, holder: LifecycleOwnerViewBindHolder) {

        if (checkStatus == CheckBoxStatus.CheckAll) {
            data.isCheck = true
        } else if (checkStatus == CheckBoxStatus.UncheckAll) {
            data.isCheck = false
        }

        if (data.isCheck) {
            addItemToSelection(position, data)
        } else {
            removeItemFromSelection(position)
        }
    }

    protected open fun doWhenInit(data: CB, position: Int, holder: LifecycleOwnerViewBindHolder) {
        doCheckLogicWhenInit(data, position, holder)
    }

    override fun onBindViewHolder(holder: LifecycleOwnerViewBindHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val data = getItem(position)

        if (data?.isInit == false) {
            doWhenInit(data, position, holder)
            //最後改變init值，代表處理過bind，剩下的資料更換邏輯就交給live data
            data.isInit = true
        }

        doObserveDataChange(holder)
    }

    protected open fun doObserveDataChange(holder: LifecycleOwnerViewBindHolder) {
        observeCheckboxStatus(holder)
    }

    private fun observeCheckboxStatus(holder: LifecycleOwnerViewBindHolder) {
        //觀察是否被選取，以 view holder 做為生命週期
        liveSelectCheckBoxMap.observe(holder) {
            val newPosition = holder.adapterPosition
            if (newPosition == RecyclerView.NO_POSITION) return@observe
            val selectByMapData = getItem(newPosition)
            if (selectByMapData != null) {
                val isSelect = isItemSelected(newPosition)
                onCheckboxChange(holder.binding as VB, selectByMapData, newPosition, isSelect)
            }
        }
    }

    abstract fun onCheckboxChange(binding: VB, item: CB, position: Int, isSelect: Boolean)

    private enum class CheckBoxStatus {
        Normal, CheckAll, UncheckAll
    }
}