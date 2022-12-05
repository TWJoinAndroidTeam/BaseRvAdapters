package com.example.baseadapterslibrary.module

interface IHeaderAdapterSetting {
    val shouldFadeOutHeader: Boolean
    fun isHeader(itemPosition: Int): Boolean
}

