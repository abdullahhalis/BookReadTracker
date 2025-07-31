package com.abdullahhalis.bookreadtracker.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdullahhalis.bookreadtracker.data.BookRepository
import com.abdullahhalis.bookreadtracker.ui.detail.DetailViewModel
import com.abdullahhalis.bookreadtracker.ui.home.MainViewModel

class ViewModelFactory private constructor(private val bookRepository: BookRepository): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(bookRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(bookRepository) as T
            else -> throw Throwable("Unknown Viewmodel Class: " + modelClass.name)
        }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    BookRepository.getInstance(context)
                )
            }
    }
}