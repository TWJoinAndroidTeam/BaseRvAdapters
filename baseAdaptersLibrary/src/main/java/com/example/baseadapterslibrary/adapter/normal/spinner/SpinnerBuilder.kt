package com.example.baseadapterslibrary.adapter.normal.spinner

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class SpinnerBuilder(private val context: Context, private val adapter: SpinnerAdapter<*, *>) {

    private val displayMetrics: DisplayMetrics by lazy { Resources.getSystem().displayMetrics }

    private var listener = object : ((ISpinnerUI<*>, Int) -> Unit) {
        override fun invoke(p1: ISpinnerUI<*>, p2: Int) {
            popupWindow.dismiss()
        }
    }

    private val popupWindow: PopupWindow by lazy {
        PopupWindow(context).apply {
            isOutsideTouchable = true
            isTouchable = true
            contentView = popLayout
            contentView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        }
    }

    private val defaultPadding = dp2px(3)

    private var topPadding = defaultPadding
    private var bottomPadding = defaultPadding
    private var startPadding = defaultPadding
    private var endPadding = defaultPadding

    private val popLayout: ConstraintLayout by lazy {
        val constraintLayout = ConstraintLayout(context)
        constraintLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        constraintLayout.addView(recyclerView)

        // Set constraints
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(recyclerView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.applyTo(constraintLayout)

        return@lazy constraintLayout
    }

    private val recyclerView: RecyclerView by lazy {
        RecyclerView(context).apply {
            adapter = this@SpinnerBuilder.adapter
            itemAnimator = null
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(this@SpinnerBuilder.context)
            id = UUID.randomUUID().hashCode()
        }
    }

    init {
        adapter.addOnSpinnerItemSelectListener(listener)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager): SpinnerBuilder {
        recyclerView.layoutManager = layoutManager
        return this

    }

    /**
     * 設置預設彈窗及選項間距，不設置的話，預設是3dp
     */
    fun setPadding(padding: Int): SpinnerBuilder {
        val changePadding = dp2px(padding)
        this.startPadding = changePadding
        this.endPadding = changePadding
        this.bottomPadding = changePadding
        this.topPadding = changePadding
        return this
    }

    /**
     * 設置預設彈窗及選項左右間距，不設置的話，預設是3dp
     */
    fun setHorizontaPadding(padding: Int): SpinnerBuilder {
        val changePadding = dp2px(padding)
        this.startPadding = changePadding
        this.endPadding = changePadding
        return this
    }

    /**
     * 設置預設彈窗及選項上下間距，不設置的話，預設是3dp
     */
    fun setVerticalPadding(padding: Int): SpinnerBuilder {
        val changePadding = dp2px(padding)
        this.bottomPadding = changePadding
        this.topPadding = changePadding
        return this
    }

    fun setBackgroundRes(@DrawableRes backgroundRes: Int): SpinnerBuilder {
        val backgroundDrawable = AppCompatResources.getDrawable(context, backgroundRes)
        popupWindow.setBackgroundDrawable(backgroundDrawable)

        return this
    }

    /**
     * 是否取消點擊自動隱藏spinner view
     */
    fun blockAutoHideByClick(): SpinnerBuilder {
        adapter.removeOnSpinnerItemSelectListener(listener)
        return this
    }

    /**
     * 取得最後的spinner view 及 spinner view 內部的 recyclerview
     */
    fun build(): Pair<PopupWindow, RecyclerView> {
        recyclerView.setPadding(startPadding, topPadding, endPadding, bottomPadding)
        return Pair(popupWindow, recyclerView)
    }

    private fun dp2px(dbValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dbValue.toFloat(), displayMetrics).toInt()
    }
}