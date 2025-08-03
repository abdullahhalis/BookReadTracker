package com.abdullahhalis.bookreadtracker.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.data.BookRepository
import com.abdullahhalis.bookreadtracker.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBookViewModel(private val bookRepository: BookRepository): ViewModel() {
    private val _saved: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val saved: LiveData<Event<Boolean>> get() = _saved

    fun insertBook(book: Book){
        viewModelScope.launch(Dispatchers.IO) {
            bookRepository.insertBook(book)
        }
        _saved.value = Event(true)
    }
}