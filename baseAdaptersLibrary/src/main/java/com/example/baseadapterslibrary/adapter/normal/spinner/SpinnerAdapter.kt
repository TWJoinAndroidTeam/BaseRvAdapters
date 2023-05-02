package com.example.baseadapterslibrary.adapter.normal.spinner

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.BaseRvAdapter
import com.example.baseadapterslibrary.view_holder.BaseViewBindHolder

abstract class SpinnerAdapter<VB : ViewBinding, SP : ISpinnerUI<*>> : BaseRvAdapter<VB, SP>() {

    private var spinnerSelectListenerList: MutableList<((SP, position: Int) -> Unit)?> = mutableListOf()

    fun addOnSpinnerItemSelectListener(listener: (item: SP, position: Int) -> Unit) {
        spinnerSelectListenerList.add(listener)
    }

    fun removeOnSpinnerItemSelectListener(listener: (item: SP, position: Int) -> Unit) {
        spinnerSelectListenerList.remove(listener)
    }

    fun onSpinnerItemClick(item: SP, position: Int) {
        if (item.canSelect) {
            spinnerSelectListenerList.forEach {
                it?.invoke(item, position)
            }
        }
    }
}
