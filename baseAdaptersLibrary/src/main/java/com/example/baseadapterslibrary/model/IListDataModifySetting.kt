package com.example.baseadapterslibrary.model

interface IListDataModifySetting<T> {

    fun removeItem(item: T, position: Int)

    fun addItem(data: T)

    fun setItem(position: Int, data: T)

    fun rangeRemoveItems(positionStart:Int,list: List<T>)

    fun rangeInsertItems(positionStart:Int,list: List<T>)

}

