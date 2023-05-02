package com.example.baservadapters.model

import com.example.baseadapterslibrary.adapter.normal.spinner.ISpinnerUI
import com.example.baseadapterslibrary.model.UIText

data class TestSpinner(
    override val canSelect: Boolean,
    override val realData: Int?
) : ISpinnerUI<Int> {
    override val spinnerItemName: UIText = UIText.DynamicString(realData.toString())
}