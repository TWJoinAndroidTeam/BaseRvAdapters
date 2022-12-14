package com.example.baseadapterslibrary.adapter.normal.checkbox


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.checkboxadapterlibrary.extension.changeCheck
import com.example.baseadapterslibrary.baseAdaptersLibrary.module.ICheckBox
import com.example.baseadapterslibrary.module.ChooserMode
import com.example.baseadapterslibrary.module.ICheckBoxSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

typealias SortListIndex = Int

typealias RealDataPosition = Int

abstract class CheckBoxAdapter<VB : ViewBinding, CB : ICheckBox>(
    val inflate: Inflate<VB>,
) : RecyclerView.Adapter<CheckBoxAdapter.CheckBoxBindHolder>(), ICheckBoxSetting<CB> {

    lateinit var context: Context

    abstract val chooserMode: ChooserMode

    var selectCheckBoxMap = mutableMapOf<Int, CB>()

    protected var selectCheckBoxMultiHaveSortList = mutableListOf<Pair<RealDataPosition, CB>>()
    protected var selectCheckBoxMultiHaveSortMap = mutableMapOf<RealDataPosition, SortListIndex>()

    protected var checkBoxList: MutableList<CB> = mutableListOf()

    private var onItemClickListener: ((CB, position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (CB, position: Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxBindHolder {
        if (!::context.isInitialized) context = parent.context

        return CheckBoxBindHolder(
            inflate.invoke(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            createHolder(binding as VB, this)
        }
    }

    open suspend fun setData(checkBoxList: MutableList<CB>) {
        if (checkBoxList.isNotEmpty()) {
            selectCheckBoxMap.clear()

            for (i in checkBoxList.indices) {
                if (checkBoxList[i].isCheck) {
                    selectCheckBoxMap[i] = checkBoxList[i]
                }
            }
            updateDataSet(checkBoxList)
        }
    }

    internal open suspend fun updateDataSet(newDataSet: MutableList<CB>) = withContext(Dispatchers.Main) {

        val diff = getDiffWay(newDataSet)
        checkBoxList.clear()
        checkBoxList.addAll(newDataSet)

        withContext(Dispatchers.Main) {
            diff.dispatchUpdatesTo(this@CheckBoxAdapter)
        }

    }

    override fun onBindViewHolder(
        holder: CheckBoxBindHolder,
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
                        checkBoxList[adapterPosition],
                        adapterPosition
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CheckBoxBindHolder, position: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        val item = checkBoxList[adapterPosition]
        bind(holder.binding as VB, item, adapterPosition, holder)
        onCheckStateExchange(selectCheckBoxMap.containsKey(position), holder.binding, item, position)
    }

    override fun getItemCount() = checkBoxList.size

    /**
    ??????checkbox
     */
    fun onCheckBoxClick(cb: CB, position: Int) {

        if (chooserMode is ChooserMode.SingleChoice) {
            if (!(chooserMode as ChooserMode.SingleChoice).canRemoveSelect && cb.isCheck) return
        } else if (!cb.isCheck && chooserMode is ChooserMode.MultipleResponse && selectCheckBoxMultiHaveSortList.size >= ((chooserMode as? ChooserMode.MultipleResponse)?.selectLimitOption?.first
                ?: Int.MAX_VALUE)
        ) {
            if ((chooserMode as? ChooserMode.MultipleResponse)?.selectLimitOption?.second != true) return
            val firstSelectCB = selectCheckBoxMultiHaveSortList.first()
            if (firstSelectCB != cb) {
                firstSelectCB.second.changeCheck()
                setClickLogic(false, firstSelectCB.first, firstSelectCB.second)
            }
        }

        cb.changeCheck()

        val isSelect = cb.isCheck

        setClickLogic(isSelect, position, cb)

        onItemClickListener?.invoke(cb, position)
    }

    private fun setClickLogic(isSelect: Boolean, position: Int, cb: CB) {
        when (chooserMode) {
            is ChooserMode.MultipleResponse -> {
                if (isSelect) {

                    selectCheckBoxMap[position] = cb
                    if ((chooserMode as ChooserMode.MultipleResponse).selectLimitOption != null) {
                        selectCheckBoxMultiHaveSortList.add(Pair(position, cb))
                        selectCheckBoxMultiHaveSortMap[position] = selectCheckBoxMultiHaveSortList.lastIndex
                        Log.e("indexAdd", selectCheckBoxMultiHaveSortList.lastIndex.toString())
                    }
                } else {
                    selectCheckBoxMap.remove(position)
                    val index = selectCheckBoxMultiHaveSortMap[position]

                    if (index != null) {

                        Log.e("indexRemove", index.toString())
                        selectCheckBoxMultiHaveSortList.removeAt(index)


                        for (i in selectCheckBoxMultiHaveSortList.indices) {
                            selectCheckBoxMultiHaveSortMap[selectCheckBoxMultiHaveSortList[i].first] = i
                        }
                    }
                }

                checkBoxList[position] = cb
                notifyItemChanged(position, isSelect)
            }

            is ChooserMode.SingleChoice -> {

                if (isSelect) {
                    selectCheckBoxMap.forEach {
                        if (it.value.isCheck && position != it.key) {
                            checkBoxList[it.key].changeCheck()
                            selectCheckBoxMap.remove(it.key)
                            notifyItemChanged(it.key, checkBoxList[it.key].isCheck)
                        }
                    }
                    selectCheckBoxMap[position] = cb
                    checkBoxList[position].isCheck = true
                } else {
                    selectCheckBoxMap.remove(position)
                    checkBoxList[position].isCheck = false
                }
                notifyItemChanged(position, isSelect)
            }
        }
    }

    /**
    ??????
     */
    override fun selectAll() {
        checkBoxList.forEachIndexed { index, cb ->
            cb.isCheck = true
            selectCheckBoxMap[index] = cb
        }
        notifyDataSetChanged()
    }

    /**
    ??????
     */
    override fun clearAll() {
        checkBoxList.forEachIndexed { index, cb ->
            cb.isCheck = false
        }
        selectCheckBoxMap.clear()
        notifyDataSetChanged()
    }

    /**
    ??????
     */
    override fun reverseSelect() {
        checkBoxList.forEachIndexed { index, cb ->
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
    ??????????????????????????????????????????
     */
    override fun getSelectDataPosition(): MutableList<Int> {
        return selectCheckBoxMap.keys.toMutableList()
    }

    /**
    ??????????????????????????????????????????
     */
    override fun getSelectDataList(): MutableList<CB> {
        return selectCheckBoxMap.values.toMutableList()
    }

    /**
    ???????????????????????????????????? Map
     */
    override fun getSelectDataMap(): MutableMap<Int, CB> {
        return selectCheckBoxMap
    }

    abstract fun createHolder(binding: VB, viewHolder: RecyclerView.ViewHolder)
    abstract fun bind(binding: VB, checkBox: CB, position: Int, viewHolder: CheckBoxBindHolder)
    abstract fun onCheckStateExchange(isCheck: Boolean, binding: VB, checkBox: CB, position: Int)

    class CheckBoxBindHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    open fun removeItem(position: Int) {
        checkBoxList.removeAt(position)
        selectCheckBoxMap.remove(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, checkBoxList.size)
    }

    open fun getDiffWay(newDataSet: MutableList<CB>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return checkBoxList.size
            }

            override fun getNewListSize(): Int {
                return newDataSet.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldData = newDataSet.getOrNull(oldItemPosition)
                val newData = checkBoxList.getOrNull(newItemPosition)

                return when {
                    oldData == newData || newData == null -> false
                    else -> oldData == newData
                }
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

                val oldData = newDataSet.getOrNull(oldItemPosition)
                val newData = checkBoxList.getOrNull(newItemPosition)

                return when {
                    oldData == newData || newData == null -> false
                    else -> oldData == newData
                }
            }
        })
    }
}
