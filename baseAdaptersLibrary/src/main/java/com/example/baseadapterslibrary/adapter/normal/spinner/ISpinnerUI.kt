package com.example.baseadapterslibrary.adapter.normal.spinner

interface ISpinnerUI<T> {
    val spinnerItemName: String
    val canSelect: Boolean
    val realData: T?
}

