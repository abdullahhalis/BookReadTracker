package com.abdullahhalis.bookreadtracker.ui.status

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.StatusPagerItemBinding
import com.abdullahhalis.bookreadtracker.util.BookStatusType

class StatusBookAdapter(private val onItemClickListener: (Book) -> Unit): RecyclerView.Adapter<StatusBookAdapter.PagerViewHolder>() {
    private val bookMap = LinkedHashMap<BookStatusType, List<Book>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = StatusPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int = bookMap.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val key = when (position) {
            0 -> BookStatusType.WANT_TO_READ
            1 -> BookStatusType.CURRENTLY_READING
            2 -> BookStatusType.FINISHED_READING
            else -> throw IndexOutOfBoundsException()
        }
        val pageData = bookMap[key] ?: return
        holder.bind(pageData, onItemClickListener)
    }

    fun submitData(key: BookStatusType, books: List<Book>) {
        bookMap[key] = books
        notifyDataSetChanged()
    }

    inner class PagerViewHolder(private val binding: StatusPagerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pageData: List<Book>, onItemClickListener: (Book) -> Unit){
            binding.apply {
                rvStatusBooks.layoutManager = LinearLayoutManager(root.context)
                rvStatusBooks.adapter = StatusBookItemAdapter(pageData, onItemClickListener)
            }
        }
    }
}