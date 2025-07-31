package com.abdullahhalis.bookreadtracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class Book(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int = 0,

    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    val title: String,

    @ColumnInfo(name = "genre", typeAffinity = ColumnInfo.TEXT)
    val genre: String,

    @ColumnInfo(name = "totalPage", typeAffinity = ColumnInfo.INTEGER)
    val totalPage: Int,

    @ColumnInfo(name = "author", typeAffinity = ColumnInfo.TEXT)
    val author: String,

    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.TEXT)
    var status: String,

    @ColumnInfo(name = "readingProgress", typeAffinity = ColumnInfo.INTEGER)
    var readingProgress: Int,

    @ColumnInfo(name = "personalNote", typeAffinity = ColumnInfo.TEXT)
    var personalNote: String,

    @ColumnInfo(name = "bookAddedInMillis", typeAffinity = ColumnInfo.INTEGER)
    val bookAddedInMillis: Long,
)