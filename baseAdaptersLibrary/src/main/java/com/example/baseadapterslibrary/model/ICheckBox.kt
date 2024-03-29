package com.example.baseadapterslibrary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

interface ICheckBox : Cloneable {
    var isCheck: @RawValue Boolean

    override fun clone(): Any {
        return super.clone()
    }
}

/**
 * @property isInit 由於paging data 無法預先取得所有資料，所以 isExpand 不適用，並增加isInit判斷資料是否初次載入
 */
interface IPaging {
    var isInit: @RawValue Boolean
}

interface IExpandable {
    var isExpand: Boolean
}

interface IPagingCheckBox : ICheckBox, IPaging


interface IPagingExpandableCheckBox : IPagingCheckBox, IExpandable

interface IExpandableCheckBox : ICheckBox, IExpandable


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
) : Parcelable, IExpandableCheckBox

@Parcelize
data class PagingCheckBoxModel<T>(
    val item: @RawValue T,
    override var isCheck: Boolean,
    override var isInit: Boolean,
) : IPagingCheckBox, Parcelable