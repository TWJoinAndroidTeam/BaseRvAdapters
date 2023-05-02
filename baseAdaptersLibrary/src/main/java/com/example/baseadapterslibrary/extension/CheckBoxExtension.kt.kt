package com.example.baseadapterslibrary.extension

import com.example.baseadapterslibrary.model.CheckBoxModel
import com.example.baseadapterslibrary.model.ExpandableCheckBoxModel
import com.example.baseadapterslibrary.model.ICheckBox


fun ICheckBox.changeCheck() {
    isCheck = !isCheck
}

/**
 * 將 list 轉成 checkbox list
 * @param defaultCheckPosition 預設被選選項
 */
fun <T> List<T>.toCheckBoxModelList(defaultCheckPosition: Int? = null): MutableList<CheckBoxModel<T>> {
    val checkBoxList = mutableListOf<CheckBoxModel<T>>()
    if (this.isNotEmpty()) {
        for (i in this.indices) {
            val isDefaultCheck = defaultCheckPosition == i
            val checkBoxModel = CheckBoxModel(this[i], isDefaultCheck)
            checkBoxList.add(checkBoxModel)
        }
    }

    return checkBoxList
}

fun <T> List<T>.toExpandableCheckBoxModelList(defaultCheckPosition: Int? = null): MutableList<ExpandableCheckBoxModel<T>> {
    val checkBoxList = mutableListOf<ExpandableCheckBoxModel<T>>()
    if (this.isNotEmpty()) {
        for (i in this.indices) {
            val isDefaultCheck = defaultCheckPosition == i
            val checkBoxModel = ExpandableCheckBoxModel(this[i], isDefaultCheck, false)
            checkBoxList.add(checkBoxModel)
        }
    }
    return checkBoxList
}

fun <T> List<T>.toExpandableCheckBoxModelList(checkLogic: (item: T) -> Boolean): MutableList<ExpandableCheckBoxModel<T>> {
    val checkBoxList = mutableListOf<ExpandableCheckBoxModel<T>>()
    if (this.isNotEmpty()) {
        forEach {
            val isDefaultCheck = checkLogic.invoke(it)
            val checkBoxModel = ExpandableCheckBoxModel(it, isDefaultCheck, false)
            checkBoxList.add(checkBoxModel)
        }
    }
    return checkBoxList

}