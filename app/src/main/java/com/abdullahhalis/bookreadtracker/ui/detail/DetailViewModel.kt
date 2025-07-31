package com.abdullahhalis.bookreadtracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.data.BookRepository
import com.abdullahhalis.bookreadtracker.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val bookRepository: BookRepository): ViewModel() {
    private val _bookId: MutableLiveData<Int> = MutableLiveData()
    val book: LiveData<Book?> = _bookId.switchMap { bookId ->
        bookRepository.getBook(bookId)
    }

    private val _updated: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val updated: LiveData<Event<Boolean>> get() = _updated

    private val _deleted: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val deleted: LiveData<Event<Boolean>> get() = _deleted

    fun setBookId(bookId: Int) {
        _bookId.value = bookId
    }

    fun updateBook(newStatus: String, newReadingProgress: Int, newPersonalNote: String) {
        val updatedBook = book.value

        updatedBook?.apply {
            status = newStatus
            readingProgress = newReadingProgress
            personalNote = newPersonalNote
        }

        updatedBook?.let {
            viewModelScope.launch(Dispatchers.IO) { bookRepository.updateBook(updatedBook) }
            _updated.value = Event(true)
        }
    }

    fun deleteBook() {
        book.value?.let {
            viewModelScope.launch(Dispatchers.IO) { bookRepository.deleteBook(it) }
            _deleted.value = Event(true)
        }
    }
}