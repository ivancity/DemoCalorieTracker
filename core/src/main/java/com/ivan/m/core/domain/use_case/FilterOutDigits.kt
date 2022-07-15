package com.ivan.m.core.domain.use_case

/**
 * We can call this class as a function. We are overwriting the invoke method.
 * This use case will get a number as string with characters, and only return the
 * digit portion of this string.
 */
class FilterOutDigits {

    operator fun invoke(text: String): String {
        return text.filter { it.isDigit() }
    }

}