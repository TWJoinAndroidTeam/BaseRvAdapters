package com.example.baseadapterslibrary.adapter.normal

import androidx.viewbinding.ViewBinding
import com.example.baseadapterslibrary.adapter.normal.checkbox.Inflate
import com.example.baseadapterslibrary.module.IItemTouchHelper
import com.example.baseadapterslibrary.module.OnStartDragListener
import java.util.*

abstract class BaseDraggableRvAdapter<VB : ViewBinding, DATA>(internal open val onStartDragListener: OnStartDragListener) : BaseRvAdapter<VB, DATA>(),
    IItemTouchHelper {

    private var onItemMoveListener: ((fromPosition: Int, toPosition: Int) -> Unit)? = null
    fun setOnItemMoveListener(listener: (fromPosition: Int, toPosition: Int) -> Unit) {
        onItemMoveListener = listener
    }

    protected var onItemDeleteListener: ((deletePosition: Int) -> Unit)? = null

    @JvmName("setOnItemDeleteListener1")
    fun setOnItemDeleteListener(listener: (deletePosition: Int) -> Unit) {
        onItemDeleteListener = listener
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(dataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        onItemMoveListener?.invoke(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        onItemDeleteListener?.invoke(position)
    }

}