package com.example.baseadapterslibrary.model

import androidx.viewbinding.ViewBinding

interface IExpandSetting<VB : ViewBinding> {

    /**
     * 改變全部的開展狀態
     */
    fun changeExpandAllData(isExpand: Boolean)

    /**
     * 組件默認點擊狀態
     */
    fun clickExpandItem(binding: VB, adapterPosition: Int)

    /**
     * 指定任意位置組件更改為任意狀態
     */
    fun changeExpandItem(binding: VB, isExpand: Boolean, adapterPosition: Int)
}