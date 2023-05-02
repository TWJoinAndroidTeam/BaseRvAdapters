package com.example.baseadapterslibrary.model

interface IHeaderAdapterSetting {
    val shouldFadeOutHeader: Boolean
    fun isHeader(itemPosition: Int): Boolean
}

