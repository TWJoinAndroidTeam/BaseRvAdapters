package com.example.baseadapterslibrary.module


/**
 * 選擇上限數值
 */
typealias SelectLimit = Int

/**
 * 選滿後是否能選更多
 */
typealias CanOverSelect = Boolean

typealias SelectLimitOption = Pair<SelectLimit, CanOverSelect>


sealed class ChooserMode {
    /**
     *單選
     * @param canRemoveSelect 是否可以反選
     */
    class SingleChoice(val canRemoveSelect: Boolean) : ChooserMode()

    /**
     *多選
     * @param selectLimitOption 選擇上限設定
     *
     */
    class MultipleResponse(val selectLimitOption: SelectLimitOption? = null) : ChooserMode()
}
