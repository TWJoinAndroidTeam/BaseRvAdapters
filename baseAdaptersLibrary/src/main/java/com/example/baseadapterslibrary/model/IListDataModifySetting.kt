package com.example.baseadapterslibrary.model

interface IListDataModifySetting<T> {

    suspend fun updateDataSet(newDataSet: MutableList<T>)

    fun setRemoveItemListener(callback: (item: T?, position: Int) -> Unit)

    fun setInsertItemListener(callback: (item: T, position: Int) -> Unit)

    fun setAddItemListener(callback: (item: T) -> Unit)

    fun setItemRangeRemoveListener(callback: (positionStart: Int, list: List<T>) -> Unit)

    fun setItemRangeInsertListener(callback: (positionStart: Int, list: List<T>) -> Unit)

}

