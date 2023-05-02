package com.example.baseadapterslibrary.adapter.normal.spinner

interface ISpinnerUI<T> {
    val canSelect: Boolean
    val realData: T?
}