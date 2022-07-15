package com.ivan.m.core.util

import android.content.Context

/**
 * We have two types of string. One that we do not have access to the context but
 * we have the string itself. Or if we have the UI layer and access to the context,
 * then we can construct our text.
 */
sealed class UiText {
    data class DynamicString(val text: String): UiText()
    data class StringResource(val resId: Int): UiText()

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}