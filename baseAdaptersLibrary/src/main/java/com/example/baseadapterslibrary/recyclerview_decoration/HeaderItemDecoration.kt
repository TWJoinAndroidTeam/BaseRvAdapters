package com.example.baseadapterslibrary.recyclerview_decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.module.IHeaderAdapterSetting

class HeaderItemDecoration(
    mainRecyclerview: RecyclerView,
    private val iHeaderAdapterSetting: IHeaderAdapterSetting
) : RecyclerView.ItemDecoration() {

    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    init {
        mainRecyclerview.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                // clear saved header as it can be outdated now
                currentHeader = null
            }
        })

        mainRecyclerview.doOnEachNextLayout {
            // clear saved layout as it may need layout update
            currentHeader = null
        }
        // handle click on sticky header
        mainRecyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView,
                motionEvent: MotionEvent
            ): Boolean {
                return if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val isClickHeader = motionEvent.y <= (currentHeader?.second?.itemView?.bottom ?: 0)
                    when (currentHeader?.second) {

                    }
                    return isClickHeader
                } else false
            }
        })
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //val topChild = parent.getChildAt(0) ?: return
        val topChild = parent.findChildViewUnder(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat() /*+ (currentHeader?.second?.itemView?.height ?: 0 )*/
        ) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return

        val contactPoint = headerView.bottom + parent.paddingTop
        val childInContact = getChildInContact(parent, contactPoint) ?: return


        val position = parent.getChildAdapterPosition(childInContact)

        if (position == RecyclerView.NO_POSITION) return
        if (iHeaderAdapterSetting.isHeader(position)) {
            moveHeader(c, headerView, childInContact, parent.paddingTop)
            return
        }

        drawHeader(c, headerView, parent.paddingTop)
    }

    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) {
            return null
        }
        val headerPosition = getHeaderPositionForItem(itemPosition)
        if (headerPosition == RecyclerView.NO_POSITION) return null
        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null
        // if match reuse viewHolder
        if (currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType) {
            return currentHeader?.second?.itemView
        }

        val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
        if (headerHolder != null) {
            parent.adapter?.onBindViewHolder(headerHolder, headerPosition)
            fixLayoutSize(parent, headerHolder.itemView)
            // save for next draw
            currentHeader = headerPosition to headerHolder
        }
        return headerHolder?.itemView
    }

    private fun drawHeader(c: Canvas, header: View, paddingTop: Int) {
        c.save()
        c.translate(0f, paddingTop.toFloat())
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View, paddingTop: Int) {
        c.save()
        if (!iHeaderAdapterSetting.shouldFadeOutHeader) {
            c.clipRect(0, paddingTop, c.width, paddingTop + currentHeader.height)
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                c.saveLayerAlpha(
                    RectF(0f, 0f, c.width.toFloat(), c.height.toFloat()),
                    (((nextHeader.top - paddingTop) / nextHeader.height.toFloat()) * 255).toInt()
                )
            } else {
                c.saveLayerAlpha(
                    0f, 0f, c.width.toFloat(), c.height.toFloat(),
                    (((nextHeader.top - paddingTop) / nextHeader.height.toFloat()) * 255).toInt(),
                    Canvas.ALL_SAVE_FLAG
                )
            }

        }
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat() /*+ paddingTop*/)

        currentHeader.draw(c)
        if (iHeaderAdapterSetting.shouldFadeOutHeader) {
            c.restore()
        }
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            if (mBounds.bottom > contactPoint) {
                if (mBounds.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    /**
     * Properly measures and layouts the top sticky header.
     *
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition
        do {
            if (iHeaderAdapterSetting.isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return headerPosition
    }
}

inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
        action(
            view
        )
    }
}