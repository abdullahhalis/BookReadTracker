package com.abdullahhalis.bookreadtracker.ui.status

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.ListBookItemBinding

class StatusBookItemAdapter(
    private val listItem: List<Book>,
    private val onItemClickListener: (Book) -> Unit
): RecyclerView.Adapter<StatusBookItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listItem[position], onItemClickListener)
    }

    inner class ViewHolder internal constructor(private val binding: ListBookItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, clickListener: (Book) -> Unit) {
            binding.apply {
                tvBookTitle.text = book.title
                tvBookGenre.text = book.genre
                tvBookAuthor.text = book.author
                tvBookTotalPages.text = "${book.totalPage}"
                root.setOnClickListener { clickListener(book) }
            }
        }
    }
}