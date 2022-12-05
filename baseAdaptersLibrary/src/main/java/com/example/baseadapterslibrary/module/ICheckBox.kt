package com.example.baseadapterslibrary.baseAdaptersLibrary.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

interface ICheckBox {
    var isCheck: @RawValue Boolean
}

/**
 * @property isInit
 * 由於paging data 無法預先取得所有資料，所以 isExpand 不適用，並增加isInit判斷資料是否初次載入
 */
interface IPagingCheckBox : ICheckBox {
    var isInit: @RawValue Boolean
}

interface ExpandableICheckBox : ICheckBox {
    var isExpand: Boolean
}


@Parcelize
data class CheckBoxModel<T>(
    val item: @RawValue T?,
    override var isCheck: Boolean,
) : ICheckBox, Parcelable

@Parcelize
data class ExpandableCheckBoxModel<T>(
    val item: @RawValue T?,
    override var isCheck: Boolean,
    override var isExpand: Boolean,
) : ICheckBox, Parcelable, ExpandableICheckBox

@Parcelize
data class PagingCheckBoxModel<T>(
    val item: @RawValue T,
    override var isCheck: Boolean,
    override var isInit: Boolean,
) : IPagingCheckBox, Parcelable