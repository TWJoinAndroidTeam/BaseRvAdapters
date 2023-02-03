package com.example.baseadapterslibrary.extension

/**
 * 新增或重置List資料
 */
fun <E> MutableList<E>.addOrReplace(position: Int, e: E) {
    val originModel = getOrNull(position)
    if (originModel == null) {
        add(position, e)
    } else {
        set(position, e)
    }
}

