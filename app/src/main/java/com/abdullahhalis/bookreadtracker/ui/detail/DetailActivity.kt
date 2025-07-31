package com.abdullahhalis.bookreadtracker.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.databinding.ActivityDetailBinding
import com.abdullahhalis.bookreadtracker.util.BOOK_ID
import com.abdullahhalis.bookreadtracker.util.BookStatusType
import com.abdullahhalis.bookreadtracker.util.ViewModelFactory
import com.abdullahhalis.bookreadtracker.util.convertLongToTime
import com.abdullahhalis.bookreadtracker.util.toBookStatusType

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels { ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbarDetailActivity)
        supportActionBar?.setTitle(R.string.title_activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val bookId = intent.getIntExtra(BOOK_ID, 0)
        detailViewModel.setBookId(bookId)

        detailViewModel.book.observe(this) { book ->
            book?.let {
                with(book) {
                    binding.apply {
                        tvBookTitleValue.text = title
                        tvBookGenreValue.text = genre
                        tvBookTotalPageValue.text = "$totalPage"
                        tvBookAuthorValue.text = author
                        tvBookAddedAtValue.text = bookAddedInMillis.convertLongToTime()

                        val bookStatus = status.toBookStatusType()

                        spinnerBookStatusValue.setSelection(BookStatusType.entries.indexOf(bookStatus))
                        tietReadingProgressDetail.setText("$readingProgress")
                        tietPersonalNoteDetail.setText(personalNote)
                    }
                }
            }
        }
        detailViewModel.updated.observe(this) { event ->
            val updated = event.getContentIfNotHandled()

            if (updated == true) {
                Toast.makeText(
                    this,
                    getString(R.string.book_update_success_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        detailViewModel.deleted.observe(this) { event ->
            val deleted = event.getContentIfNotHandled()

            if (deleted == true) {
                Toast.makeText(
                    this,
                    getString(R.string.book_delete_success_message),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

        binding.apply {
            spinnerBookStatusValue.apply {
                val listOfBookStatus = BookStatusType.entries.map { it.value }
                adapter = ArrayAdapter(
                    this@DetailActivity,
                    android.R.layout.simple_list_item_1,
                    listOfBookStatus
                )
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (position) {
                            1 -> {
                                tilPersonalNoteDetail.visibility =
                                    View.GONE
                                tilReadingProgressDetail.visibility =
                                    View.VISIBLE
                            }

                            2 -> {
                                tilPersonalNoteDetail.visibility =
                                    View.VISIBLE
                                tilReadingProgressDetail.visibility =
                                    View.GONE
                            }

                            else -> {
                                tilReadingProgressDetail.visibility =
                                    View.GONE
                                tilPersonalNoteDetail.visibility =
                                    View.GONE
                            }
                        }
                    }
                }
            }
            btnUpdate.setOnClickListener {
                val newStatus = spinnerBookStatusValue.selectedItem.toString().toBookStatusType()
                var newReadingProgress = 0
                val newPersonalNote = tietPersonalNoteDetail.text.toString()

                when(newStatus) {
                    BookStatusType.WANT_TO_READ -> newReadingProgress = 0
                    BookStatusType.FINISHED_READING -> newReadingProgress = detailViewModel.book.value?.totalPage ?: 0
                    BookStatusType.CURRENTLY_READING -> newReadingProgress = tietReadingProgressDetail.text.toString().toIntOrNull() ?: 0
                }
                detailViewModel.updateBook(newStatus.value, newReadingProgress, newPersonalNote)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(getString(R.string.delete_alert))
                    setNegativeButton(getString(R.string.no), null)
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        detailViewModel.deleteBook()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}