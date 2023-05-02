package com.example.baseadapterslibrary.model

import androidx.recyclerview.widget.RecyclerView

fun interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
}