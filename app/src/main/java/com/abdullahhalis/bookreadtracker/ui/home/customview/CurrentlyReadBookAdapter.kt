package com.abdullahhalis.bookreadtracker.ui.home.customview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.CurrentlyReadBookItemBinding
import kotlin.math.floor

class CurrentlyReadBookAdapter: ListAdapter<Book, CurrentlyReadBookAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var listBook = ArrayList<Book>()
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = CurrentlyReadBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBook[position])
    }

    override fun getItemCount(): Int = listBook.size

    fun submitData(newData: List<Book>) {
        listBook.clear()
        listBook.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(book: Book)
    }

    inner class ViewHolder internal constructor(private val itemViewBinding: CurrentlyReadBookItemBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(book: Book) {
            val readingProgressInPercentage = floor((book.readingProgress.toDouble() / book.totalPage.toDouble()) * 100).toInt()
            itemViewBinding.apply {
                tvBookTitle.text = book.title
                tvBookGenre.text = book.genre
                tvReadingProgressValue.text = itemView.context.resources.getString(R.string.reading_progress_value, readingProgressInPercentage)
                pbReadingProgress.progress = readingProgressInPercentage
                itemView.setOnClickListener{
                    onItemClickCallBack.onItemClicked(book)
                }
            }
        }
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