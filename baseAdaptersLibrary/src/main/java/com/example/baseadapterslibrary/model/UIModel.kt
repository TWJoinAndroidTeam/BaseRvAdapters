package com.example.baseadapterslibrary.model

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class UIText : Parcelable {

    @Parcelize
    data class DynamicString(val text: String) : UIText(), Parcelable

    @Parcelize
    data class StringResource(val resId: Int) : UIText(), Parcelable {

        var formatArgs: Array<out Any?> = arrayOf()

        constructor(resId: Int, vararg formatArgs: Any?) : this(resId) {
            this.formatArgs = formatArgs
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> {
                if (this.formatArgs.isEmpty()) {
                    context.getString(resId)
                } else {
                    context.getString(resId, *formatArgs)
                }
            }
        }
    }
}
