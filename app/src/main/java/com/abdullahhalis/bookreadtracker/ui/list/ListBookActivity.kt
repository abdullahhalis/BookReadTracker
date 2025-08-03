package com.abdullahhalis.bookreadtracker.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.ActivityListBookBinding
import com.abdullahhalis.bookreadtracker.ui.add.AddBookActivity
import com.abdullahhalis.bookreadtracker.ui.detail.DetailActivity
import com.abdullahhalis.bookreadtracker.util.BOOK_ID
import com.abdullahhalis.bookreadtracker.util.BookSortType
import com.abdullahhalis.bookreadtracker.util.Event
import com.abdullahhalis.bookreadtracker.util.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ListBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBookBinding
    private lateinit var listBookAdapter: ListBookAdapter
    private val viewModel: ListBookViewModel by viewModels { ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbarListBookActivity)
        supportActionBar?.setTitle(R.string.title_activity_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        listBookAdapter = ListBookAdapter { book ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(BOOK_ID, book.id)
            startActivity(intent)
        }

        binding.rvListBooks.apply {
            adapter = listBookAdapter
            layoutManager = LinearLayoutManager(this@ListBookActivity)
        }

        binding.fabAddBook.setOnClickListener{
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }

        viewModel.books.observe(this) { books ->
            listBookAdapter.submitData(lifecycle, books)
        }

        listBookAdapter.addLoadStateListener { loadstate ->
            val isEmpty = listBookAdapter.itemCount == 0 && loadstate.source.refresh is LoadState.NotLoading
            binding.tvEmptyList.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
        val itemTouchHelper = ItemTouchHelper(ItemTouchCallBack())
        itemTouchHelper.attachToRecyclerView(binding.rvListBooks)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_sort -> {
                val anchor = findViewById<View>(R.id.action_sort)
                PopupMenu(this, anchor).run {
                    menuInflater.inflate(R.menu.menu_sort_book, menu)

                    setOnMenuItemClickListener {
                        viewModel.changeBookSortType(
                            when(it.itemId) {
                                R.id.sort_date_added -> BookSortType.DATE_ADDED
                                R.id.sort_title -> BookSortType.TITLE
                                R.id.sort_genre -> BookSortType.GENRE
                                R.id.sort_author -> BookSortType.AUTHOR
                                else -> BookSortType.TITLE
                            }
                        )
                        true
                    }
                    show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showSnackBar(eventMessage: Event<Int>) {
        val message = eventMessage.getContentIfNotHandled() ?: return
        Snackbar.make(
            binding.root,
            getString(message),
            Snackbar.LENGTH_SHORT
        ).setAction(getString(R.string.undo)){
            viewModel.insertBook(viewModel.undo.value?.getContentIfNotHandled() as Book)
        }.show()
    }

    inner class ItemTouchCallBack: ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.RIGHT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val book = (viewHolder as ListBookAdapter.BookViewHolder).getBook()
            viewModel.deleteBook(book)
            viewModel.snackBarText.value?.let { showSnackBar(it) }
        }
    }
}