package com.example.baseadapterslibrary.module

sealed class ChooserMode {
    /**
     *單選
     * @param canRemoveSelect 是否可以反選
     */
    class SingleChoice(val canRemoveSelect: Boolean) : ChooserMode()

    /**
     *多選
     */
    object MultipleResponse : ChooserMode()
}
