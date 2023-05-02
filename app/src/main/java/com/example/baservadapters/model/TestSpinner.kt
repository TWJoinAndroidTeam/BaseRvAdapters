package com.example.baservadapters.model

import com.example.baseadapterslibrary.adapter.normal.spinner.ISpinnerUI

data class TestSpinner(
    override val canSelect: Boolean,
    override val realData: Int?
) : ISpinnerUI<Int> {
    override val spinnerItemName = realData.toString()
}