package com.example.baseadapterslibrary.adapter.normal.spinner

import com.example.baseadapterslibrary.model.UIText

interface ISpinnerUI<T> {
    val spinnerItemName: UIText?
    val canSelect: Boolean
    val realData: T?
}

