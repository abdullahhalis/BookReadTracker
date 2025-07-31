package com.abdullahhalis.bookreadtracker.util

import androidx.sqlite.db.SimpleSQLiteQuery

object QueryUtil {
    fun sortedBookQuery(bookSortType: BookSortType):SimpleSQLiteQuery {
        val query = StringBuilder().append("SELECT * FROM book ORDER BY ")
        when(bookSortType) {
            BookSortType.DATE_ADDED -> {
                query.append("bookAddedInMillis DESC")
            }
            BookSortType.TITLE -> {
                query.append("title DESC")
            }
            BookSortType.GENRE -> {
                query.append("genre DESC")
            }
            BookSortType.AUTHOR -> {
                query.append("author DESC")
            }
        }
        return SimpleSQLiteQuery(query.toString())
    }
}