package com.abdullahhalis.bookreadtracker.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.ListBookItemBinding

class ListBookAdapter(private val clickListener: (Book) -> Unit): PagingDataAdapter<Book, ListBookAdapter.BookViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ListBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        getItem(position)?.let{
            holder.bind(it, clickListener)
        }
    }

    inner class BookViewHolder(private val binding: ListBookItemBinding): RecyclerView.ViewHolder(binding.root) {
        private lateinit var book: Book

        fun bind(book: Book, clickListener: (Book) -> Unit) {
            this.book = book

            binding.apply {
                book.apply {
                    tvBookTitle.text = title
                    tvBookGenre.text = genre
                    tvBookAuthor.text = author
                    tvBookTotalPages.text = itemView.context.getString(R.string.total_pages, totalPage)
                }
            }

            itemView.setOnClickListener { clickListener(book) }
        }

        fun getBook(): Book = book
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}