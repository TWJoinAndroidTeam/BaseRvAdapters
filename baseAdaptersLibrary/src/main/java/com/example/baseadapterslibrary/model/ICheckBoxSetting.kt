package com.example.baseadapterslibrary.model

interface ICheckBoxSetting<CB : ICheckBox> {
    /**
    全選
     */
    fun selectAll()

    /**
    清除
     */
    fun clearAll()

    /**
    反選
     */
    fun reverseSelect()

    /**
    取得所有被選擇項目的陣列位置
     */
    fun getSelectDataPosition(): List<Int>

    /**
    取得所有被選擇項目的資料陣列
     */
    fun getSelectDataList(): List<CB>

    /**
    取得所有被選擇項目的資料陣列
     */
    fun getSelectDataMap(): MutableMap<Int, CB>
}