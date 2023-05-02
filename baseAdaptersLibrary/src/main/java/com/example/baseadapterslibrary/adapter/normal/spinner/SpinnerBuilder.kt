package com.example.baseadapterslibrary.adapter.normal.spinner

import android.content.Context
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseadapterslibrary.recyclerview_decoration.RvDecoration
import java.util.*


class SpinnerBuilder(private val context: Context, private val adapter: SpinnerAdapter<*, *>) {

    init {
        adapter.addOnSpinnerItemSelectListener { iSpinnerUI, position ->
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

    private var topPadding = 3
    private var bottomPadding = 3
    private var startPadding = 3
    private var endPadding = 3

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
            layoutManager = LinearLayoutManager(this@SpinnerBuilder.context)
            id = UUID.randomUUID().hashCode()
        }
    }

    fun changePadding(padding: Int): SpinnerBuilder {
        this.startPadding = padding
        this.endPadding = padding
        this.bottomPadding = padding
        this.topPadding = padding
        return this
    }

    fun changeHorizontaPadding(padding: Int): SpinnerBuilder {
        this.startPadding = padding
        this.endPadding = padding
        return this
    }

    fun changeVerticalPadding(padding: Int): SpinnerBuilder {
        this.bottomPadding = padding
        this.topPadding = padding
        return this
    }

    fun setBackgroundRes(@DrawableRes backgroundRes: Int): SpinnerBuilder {
        val backgroundDrawable = AppCompatResources.getDrawable(context, backgroundRes)
        popupWindow.setBackgroundDrawable(backgroundDrawable)

        return this
    }

    fun build(): PopupWindow {
        recyclerView.setPadding(startPadding, topPadding, endPadding, bottomPadding)
        return popupWindow
    }
}