package com.abdullahhalis.bookreadtracker.util

enum class BookStatusType(val value: String) {
    WANT_TO_READ(WANT_TO_READ_VALUE),
    CURRENTLY_READING(CURRENTLY_READING_VALUE),
    FINISHED_READING(FINISHED_READING_VALUE)
}

fun String.toBookStatusType(): BookStatusType {
    return when (this) {
        WANT_TO_READ_VALUE -> BookStatusType.WANT_TO_READ
        CURRENTLY_READING_VALUE -> BookStatusType.CURRENTLY_READING
        FINISHED_READING_VALUE -> BookStatusType.FINISHED_READING
        else -> BookStatusType.WANT_TO_READ
    }
}