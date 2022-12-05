package com.example.baseadapterslibrary.module

import androidx.recyclerview.widget.RecyclerView

fun interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
}