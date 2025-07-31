package com.abdullahhalis.bookreadtracker.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.abdullahhalis.bookreadtracker.util.BookSortType
import com.abdullahhalis.bookreadtracker.util.CURRENTLY_READING_VALUE
import com.abdullahhalis.bookreadtracker.util.QueryUtil

class BookRepository(private val bookDao: BookDao) {

    fun getListOfCurrentlyReadBooks(): LiveData<List<Book>> = bookDao.getListOfBookByReadingStatus(
        CURRENTLY_READING_VALUE)

    fun getListOfBookByReadingStatus(status: String): LiveData<List<Book>> = bookDao.getListOfBookByReadingStatus(status)

    fun getAllBooks(bookSortType: BookSortType): LiveData<PagingData<Book>> {
        val query = QueryUtil.sortedBookQuery(bookSortType)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = ENABLE_PLACEHOLDER
            ),
            pagingSourceFactory = { bookDao.getAllBooks(query) }
        ).liveData
    }

    fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    fun getBook(bookId: Int): LiveData<Book?> = bookDao.getBook(bookId)

    fun updateBook(updatedBook: Book) {
        bookDao.updateBook(updatedBook)
    }

    fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }

    fun getTotalOfCurrentlyReadBook() = bookDao.getTotalOfCurrentlyReadBooks()

    companion object {
        private const val PAGE_SIZE = 10
        private const val ENABLE_PLACEHOLDER = true

        @Volatile
        private var instance: BookRepository? = null

        fun getInstance(context: Context): BookRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = BookDatabase.getInstance(context)
                    instance = BookRepository(database.bookDao())
                }
                return instance as BookRepository
            }
        }
    }
}