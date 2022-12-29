package com.example.baseadapterslibrary.module

sealed class ChooserMode {
    /**
     *單選
     * @param canRemoveSelect 是否可以反選
     */
    class SingleChoice(val canRemoveSelect: Boolean) : ChooserMode()

    /**
     *多選
     * @param selectLimit 選擇上限
     */
    class MultipleResponse(val selectLimit: Int? = null, val canRemoveAlreadySelect: Boolean) : ChooserMode()
}
