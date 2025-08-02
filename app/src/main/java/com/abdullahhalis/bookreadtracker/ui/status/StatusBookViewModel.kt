package com.abdullahhalis.bookreadtracker.ui.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.data.BookRepository
import com.abdullahhalis.bookreadtracker.util.CURRENTLY_READING_VALUE
import com.abdullahhalis.bookreadtracker.util.FINISHED_READING_VALUE
import com.abdullahhalis.bookreadtracker.util.WANT_TO_READ_VALUE

class StatusBookViewModel(bookRepository: BookRepository): ViewModel() {
    var tabPosition: Int = 1

    val booksStatusWantToRead: LiveData<List<Book>> = bookRepository.getListOfBookByReadingStatus(
        WANT_TO_READ_VALUE)
    val booksStatusCurrentlyReading: LiveData<List<Book>> = bookRepository.getListOfBookByReadingStatus(
        CURRENTLY_READING_VALUE)
    val booksStatusFinishedReading: LiveData<List<Book>> = bookRepository.getListOfBookByReadingStatus(
        FINISHED_READING_VALUE)
}