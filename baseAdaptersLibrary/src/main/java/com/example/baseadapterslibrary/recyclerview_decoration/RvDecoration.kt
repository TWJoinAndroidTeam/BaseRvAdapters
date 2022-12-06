package com.fpcc.formosa.common.view.recyclerview_decoration

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 *@param horizontalSpace;  整个RecyclerView与左右两侧的间距
 *@param verticalSpace;  整个RecyclerView与上下的间距
 *@param leftMargin;  每个item与左边的间距
 *@param topMargin;  每个item与顶部的间距
 *@param rightMargin;  每个item与右边的间距
 *@param bottomMargin;  每个item与底部的间距
 *@param firstItemMargin 第一筆item與螢幕間距
 *@param lastItemMargin 最後一筆item與螢幕間距
 */
class RvDecoration(
    val horizontalSpace: Int = 0,
    val verticalSpace: Int = 0,
    val leftMargin: Int = 0,
    val topMargin: Int = 0,
    val rightMargin: Int = 0,
    val bottomMargin: Int = 0,
    var radius: Float = 0f,
    var firstItemMargin: Int? = null,
    var lastItemMargin: Int? = null,
) : RecyclerView.ItemDecoration() {

    private var defaultRectToClip: RectF? = null

    init {
        defaultRectToClip = RectF(Float.MAX_VALUE, Float.MAX_VALUE, 0f, 0f)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position: Int = parent.getChildAdapterPosition(view)
        // 得到RecyclerView中Item的总个数
        val count = parent.adapter!!.itemCount

        if (parent.layoutManager is GridLayoutManager) { // 网格布局
            val gridLayoutManager = parent.layoutManager as GridLayoutManager
            // 得到网格布局的列数
            val spanCount: Int = gridLayoutManager.spanCount
            // 判断该网格布局是水平还是垂直
            if (LinearLayoutManager.VERTICAL == gridLayoutManager.orientation) { // 垂直
                if (spanCount == 1) { // 列数为1
                    verticalColumnOne(outRect, position, count)
                } else { // 列数大于1
                    verticalColumnMulti(outRect, position, count, spanCount)
                }
            } else if (LinearLayoutManager.HORIZONTAL == gridLayoutManager.orientation) { // 水平
                if (spanCount == 1) { // 行数为1
                    horizontalColumnOne(outRect, position, count)
                } else { // 行数大于1
                    horizontalColumnMulti(outRect, position, count, spanCount)
                }
            }
        } else if (parent.layoutManager is LinearLayoutManager) { // 线性布局
            val layoutManager: LinearLayoutManager = parent.layoutManager as LinearLayoutManager
            if (LinearLayoutManager.VERTICAL == layoutManager.orientation) { // 垂直
                verticalColumnOne(outRect, position, count)
            } else if (LinearLayoutManager.HORIZONTAL == layoutManager.orientation) { // 水平
                horizontalColumnOne(outRect, position, count)
            }
        }
    }

    /**
     * 列表垂直且列数为1
     *
     * @param outRect  包括左上右下四个参数，分别控制view左上右下的margin
     * @param position 当前view所处位置
     * @param count    RecyclerView中Item的总个数
     */
    private fun verticalColumnOne(outRect: Rect, position: Int, count: Int) {
        when (position) {
            0 -> { // 位置为0时(即第一个Item)，不设置底部间距
                outRect.set(leftMargin, firstItemMargin ?: (topMargin / 2), rightMargin, if (position==count -1) lastItemMargin ?: (bottomMargin / 2) else bottomMargin/2)
            }
            count - 1 -> { // 最后一个Item
                outRect.set(leftMargin, topMargin/2, rightMargin, lastItemMargin ?: (bottomMargin / 2))
            }
            else -> { // 中间的Item，不设置底部间距
                outRect.set(leftMargin, topMargin/2, rightMargin, bottomMargin/2)
            }
        }
    }

    /**
     * 列表垂直且列数大于1
     *
     * @param outRect   包括左上右下四个参数，分别控制view左上右下的margin
     * @param position  当前view所处位置
     * @param count     RecyclerView中Item的总个数
     * @param spanCount 布局的列数
     */
    private fun verticalColumnMulti(outRect: Rect, position: Int, count: Int, spanCount: Int) {
        // 通过计算得出总行数
        val totalRow = count / spanCount + if (count % spanCount == 0) 0 else 1
        // 计算得出当前view所在的行
        val row = position / spanCount
        // 通过对position加1对spanCount取余得到column
        // 保证column等于1为第一列，等于0为最后一个，其它值为中间列
        val column = (position + 1) % spanCount
        if (column == 1) {
            outRect.set(
                leftMargin,
                if (row == 0) topMargin else verticalSpace,
                horizontalSpace / 2,
                if (row == totalRow - 1) bottomMargin else 0
            )
        } else if (column == 0) {
            outRect.set(
                horizontalSpace / 2,
                if (row == 0) topMargin else verticalSpace,
                rightMargin,
                if (row == totalRow - 1) bottomMargin else 0
            )
        } else {
            outRect.set(
                horizontalSpace / 2,
                if (row == 0) topMargin else verticalSpace,
                horizontalSpace / 2,
                if (row == totalRow - 1) bottomMargin else 0
            )
        }
    }

    /**
     * 列表水平且行数为1
     *
     * @param outRect  包括左上右下四个参数，分别控制view左上右下的margin
     * @param position 当前view所处位置
     * @param count    RecyclerView中Item的总个数
     */
    private fun horizontalColumnOne(outRect: Rect, position: Int, count: Int) {
        if (position == 0) { // 位置为0时(即第一个Item)
            val firstMargin = if (firstItemMargin != null) firstItemMargin!! else leftMargin
            outRect.set(firstMargin, topMargin, rightMargin / 2, bottomMargin)
        } else if (position == count - 1) { // 最后一个Item
            val lastMargin = if (lastItemMargin != null) lastItemMargin!! else rightMargin
            outRect.set(leftMargin / 2, topMargin, lastMargin, bottomMargin)
        } else { // 中间的Item
            outRect.set(leftMargin / 2, topMargin, rightMargin / 2, bottomMargin)
        }
    }

    /**
     * 列表水平且行数大于1
     *
     * @param outRect   包括左上右下四个参数，分别控制view左上右下的margin
     * @param position  当前view所处位置
     * @param count     RecyclerView中Item的总个数
     * @param spanCount 布局的行数
     */
    private fun horizontalColumnMulti(outRect: Rect, position: Int, count: Int, spanCount: Int) {
        // 通过计算得出总列数
        val totalColumn = count / spanCount + if (count % spanCount == 0) 0 else 1
        // 计算得出当前view所在的列
        val column = position / spanCount
        // 通过对position加1对spanCount取余得到row
        // 保证row等于1为第一行，等于0为最后一个，其它值为中间行
        val row = (position + 1) % spanCount
        if (row == 1) {
            outRect.set(
                if (column == 0) leftMargin else horizontalSpace / 2,
                topMargin,
                if (column == totalColumn - 1) rightMargin else horizontalSpace / 2,
                0
            )
        } else if (row == 0) {
            outRect.set(
                if (column == 0) leftMargin else horizontalSpace / 2,
                verticalSpace,
                if (column == totalColumn - 1) rightMargin else horizontalSpace / 2,
                bottomMargin
            )
        } else {
            outRect.set(
                if (column == 0) leftMargin else horizontalSpace / 2,
                verticalSpace,
                if (column == totalColumn - 1) rightMargin else horizontalSpace / 2,
                0
            )
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)


        var maxBottom = 0f
        val roundRect = RectF(defaultRectToClip)
        val childRect = Rect()
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, childRect)
            maxBottom = maxBottom.coerceAtLeast(childRect.bottom.toFloat())
        }
        // has no `allowedViewTypes`
        if (roundRect == defaultRectToClip) {
            return
        }

        // used to draw other attachments on canvas after clipping (below `roundRect`)
        val otherRect = RectF(roundRect)
        otherRect.top = otherRect.bottom
        otherRect.bottom = maxBottom
        val path = Path()
        path.addRoundRect(roundRect, radius, radius, Path.Direction.CW)
        path.addRect(otherRect, Path.Direction.CW)
        c.clipPath(path)
    }
}