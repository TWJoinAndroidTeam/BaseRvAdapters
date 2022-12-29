package com.example.baservadapters.util

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue

object DimensionUtil {

    private val displayMetrics: DisplayMetrics by lazy { Resources.getSystem().displayMetrics }

    fun dp2px(dbValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dbValue.toFloat(), displayMetrics).toInt()
    }

    fun dp2px(dbValue: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dbValue, displayMetrics)
    }

    fun dp2pxWithFitRate(dbValue: Float): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dbValue, displayMetrics) * 0.96).toInt()
    }

    /**
     * 获取屏幕大小
     */
    fun getWindowSize(): Size {
        return Size(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    fun getShouldWidth(bias: Float): Float {
        return displayMetrics.widthPixels * bias
    }
}
