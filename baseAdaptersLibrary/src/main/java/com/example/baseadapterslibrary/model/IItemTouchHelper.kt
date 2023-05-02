package com.example.baseadapterslibrary.model

interface IItemTouchHelper {
    fun onItemMove(fromPosition: Int, toPosition: Int)   //處理用上下拖曳來產生換位效果
    fun onItemDismiss(position: Int)//處理用左右滑動來刪除項目
}