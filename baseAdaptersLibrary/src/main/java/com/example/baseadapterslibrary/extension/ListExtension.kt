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

fun <E> MutableList<E>.addOrReplaceList(startPosition: Int, e: List<E>) {

    val endPosition = startPosition + e.size

    for (i in startPosition..endPosition) {
        val newDataPosition = i - startPosition
        val newData = e.getOrNull(newDataPosition) ?: break
        addOrReplace(i, newData)
    }
}

