package com.abdullahhalis.bookreadtracker.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.data.BookRepository
import com.abdullahhalis.bookreadtracker.util.BookSortType
import com.abdullahhalis.bookreadtracker.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListBookViewModel(private val bookRepository: BookRepository): ViewModel() {
    private val _bookSortType = MutableLiveData<BookSortType>()

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> get() = _snackBarText

    private val _undo = MutableLiveData<Event<Book>>()
    val undo: LiveData<Event<Book>> get() = _undo

    init {
        _bookSortType.value = BookSortType.DATE_ADDED
    }

    val books: LiveData<PagingData<Book>> = _bookSortType.switchMap { sortType ->
        bookRepository.getAllBooks(sortType)
    }

    fun changeBookSortType(bookSortType: BookSortType) {
        _bookSortType.value = bookSortType
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) { bookRepository.deleteBook(book) }
        _snackBarText.value = Event(R.string.book_delete_success_message)
        _undo.value = Event(book)
    }

    fun insertBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) { bookRepository.insertBook(book) }
    }
}