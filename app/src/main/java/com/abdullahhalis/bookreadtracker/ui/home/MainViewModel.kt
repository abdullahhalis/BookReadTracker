package com.abdullahhalis.bookreadtracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.data.BookRepository

class MainViewModel(bookRepository: BookRepository): ViewModel() {
    val listOfCurrentlyReadBook: LiveData<List<Book>> = bookRepository.getListOfCurrentlyReadBooks()
}