package com.abdullahhalis.bookreadtracker.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.abdullahhalis.bookreadtracker.util.CURRENTLY_READING_VALUE

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(vararg books: Book)

    @Query("SELECT * FROM book WHERE status = :status ORDER BY bookAddedInMillis DESC")
    fun getListOfBookByReadingStatus(status: String): LiveData<List<Book>>

    @RawQuery(observedEntities = [Book::class])
    @Transaction
    fun getAllBooks(query: SupportSQLiteQuery): PagingSource<Int, Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: Book) : Long

    @Query("SELECT * FROM book WHERE id = :id ORDER BY bookAddedInMillis DESC")
    fun getBook(id: Int): LiveData<Book?>

    @Update
    fun updateBook(updatedBook: Book)

    @Delete
    fun deleteBook(book: Book)

    @Query("SELECT COUNT(*) FROM book WHERE status = '$CURRENTLY_READING_VALUE' ORDER BY bookAddedInMillis DESC")
    fun getTotalOfCurrentlyReadBooks(): Int
}