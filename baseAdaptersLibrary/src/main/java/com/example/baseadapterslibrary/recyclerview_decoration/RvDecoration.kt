package com.example.baseadapterslibrary.recyclerview_decoration

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class RvDecoration() : RecyclerView.ItemDecoration() {

    private var defaultRectToClip: RectF = RectF(Float.MAX_VALUE, Float.MAX_VALUE, 0f, 0f)


    // 每个item左右两侧的间距
    private var horizontalSpace = 0

    // 每个item上下的间距
    private var verticalSpace = 0

    // 整个RecyclerView的左间距
    private var leftMargin = 0

    // 整个RecyclerView的顶部间距
    private var topMargin = 0

    // 整个RecyclerView的右间距
    private var rightMargin = 0

    // 整个RecyclerView的底部间距
    private var bottomMargin = 0

    var radius: Float = 0f

    /**
     *  @param margin 整個rv的最外側間距
     */
    constructor(margin: Int, radius: Float = 0f) : this() {
        initSpace(horizontalSpace, verticalSpace, margin, margin, margin, margin, radius)
    }

    /**
     *  @param margin 整個rv的最外側間距
     */
    constructor(leftMargin: Int = 0, rightMargin: Int = 0, topMargin: Int = 0, bottomMargin: Int = 0, radius: Float = 0f) : this() {
        initSpace(horizontalSpace, verticalSpace, leftMargin, topMargin, rightMargin, bottomMargin, radius)
    }

    /**
     *  @param horizontalSpace 每个item左右两侧的间距
     *  @param verticalSpace 每个item上下的间距
     */
    constructor (horizontalSpace: Int, verticalSpace: Int, radius: Float = 0f) : this() {
        initSpace(horizontalSpace, verticalSpace, 0, 0, 0, 0, radius)
    }


    /**
     *  @param horizontalSpace 每个item左右两侧的间距
     *  @param verticalSpace 每个item上下的间距
     *  @param margin 整個rv的最外側間距
     */
    constructor(horizontalSpace: Int, verticalSpace: Int, margin: Int, radius: Float = 0f) : this() {
        initSpace(horizontalSpace, verticalSpace, margin, margin, margin, margin, radius)
    }

    /**
     *  @param horizontalSpace 每個item左右兩側的間距
     *  @param verticalSpace 每個item上下的間距
     *  @param leftMargin 整個Recycler View的左間距
     *  @param topMargin 整個Recycler View的頂部間距
     *  @param rightMargin 整個Recycler View的右間距
     *  @param bottomMargin 整個Recycler View的底部間距
     *  @param radius 原角度數
     */
    constructor(horizontalSpace: Int, verticalSpace: Int, leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int, radius: Float = 0f) : this() {
        initSpace(horizontalSpace, verticalSpace, leftMargin, topMargin, rightMargin, bottomMargin, radius)
    }

    private fun initSpace(horizontalSpace: Int, verticalSpace: Int, leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int, radius: Float = 0f) {
        this.horizontalSpace = horizontalSpace
        this.verticalSpace = verticalSpace
        this.leftMargin = leftMargin
        this.topMargin = topMargin
        this.rightMargin = rightMargin
        this.bottomMargin = bottomMargin
        this.radius = radius
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        // 得到当前Item在RecyclerView中的位置,从0开始
        val position = parent.getChildAdapterPosition(view)
        // 得到RecyclerView中Item的总个数
        val count = parent.adapter!!.itemCount
        if (parent.layoutManager is GridLayoutManager) { // 网格布局
            val gridLayoutManager = parent.layoutManager as GridLayoutManager?
            // 得到网格布局的列数
            val spanCount = gridLayoutManager!!.spanCount
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
            val layoutManager = parent.layoutManager as LinearLayoutManager?
            if (LinearLayoutManager.VERTICAL == layoutManager!!.orientation) { // 垂直
                verticalColumnOne(outRect, position, count)
            } else if (LinearLayoutManager.HORIZONTAL == layoutManager.orientation) { // 水平
                horizontalColumnOne(outRect, position, count)
            }
        } else if (parent.layoutManager is StaggeredGridLayoutManager) { // 流布局
            //TODO 瀑布流布局相关
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
        if (position == 0) { // 位置为0时(即第一个Item)，不设置底部间距
            outRect[leftMargin, topMargin, rightMargin] = 0
        } else if (position == count - 1) { // 最后一个Item
            outRect[leftMargin, verticalSpace, rightMargin] = bottomMargin
        } else { // 中间的Item，不设置底部间距
            outRect[leftMargin, verticalSpace, rightMargin] = 0
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
            outRect[leftMargin, topMargin, horizontalSpace / 2] = bottomMargin
        } else if (position == count - 1) { // 最后一个Item
            outRect[horizontalSpace / 2, topMargin, rightMargin] = bottomMargin
        } else { // 中间的Item
            outRect[horizontalSpace / 2, topMargin, horizontalSpace / 2] = bottomMargin
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
        Log.e("????!", radius.toString())
        path.addRoundRect(roundRect, radius, radius, Path.Direction.CW)
        path.addRect(otherRect, Path.Direction.CW)
        c.clipPath(path)
    }
}