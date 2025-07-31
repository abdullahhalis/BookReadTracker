package com.abdullahhalis.bookreadtracker.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.CurrentlyReadBookViewBinding

class CurrentlyReadBookView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs){
    private val binding: CurrentlyReadBookViewBinding =
        CurrentlyReadBookViewBinding.inflate(LayoutInflater.from(context), this, true)
    private val currentlyReadBookAdapter: CurrentlyReadBookAdapter = CurrentlyReadBookAdapter()

    init {
        binding.rvCurrentlyReadBooks.apply {
            adapter = currentlyReadBookAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setAdapterItemClickCallback(onItemClick: (Book) -> Unit) {
        currentlyReadBookAdapter.setOnItemClickCallback(object : CurrentlyReadBookAdapter.OnItemClickCallBack{
            override fun onItemClicked(book: Book) {
                onItemClick.invoke(book)
            }
        })
    }

    fun submitData(newData: List<Book>) {
        binding.apply {
            if(newData.isEmpty()) {
                tvEmptyCurrentlyReadBooks.visibility = VISIBLE
                rvCurrentlyReadBooks.visibility = GONE
            } else {
                tvEmptyCurrentlyReadBooks.visibility = GONE
                rvCurrentlyReadBooks.visibility = VISIBLE
                currentlyReadBookAdapter.submitData(newData)
            }
        }

    }
}