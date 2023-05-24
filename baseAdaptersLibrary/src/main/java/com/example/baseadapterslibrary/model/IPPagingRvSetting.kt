package com.example.baseadapterslibrary.model

interface IPPagingRvSetting<T> {

    fun setItemRemoveListener(listener:(item: T?) -> Unit)

    fun setItemAddListener(listener:(item: T) -> Unit)

    fun setItemClickListener(listener: (item: T) -> Unit)

    fun setItemSetListener(listener: (item: T) -> Unit)
}
