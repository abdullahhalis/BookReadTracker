package com.abdullahhalis.bookreadtracker.ui.status

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.databinding.ActivityStatusBookBinding
import com.abdullahhalis.bookreadtracker.ui.detail.DetailActivity
import com.abdullahhalis.bookreadtracker.util.BOOK_ID
import com.abdullahhalis.bookreadtracker.util.BookStatusType
import com.abdullahhalis.bookreadtracker.util.CURRENTLY_READING_VALUE
import com.abdullahhalis.bookreadtracker.util.FINISHED_READING_VALUE
import com.abdullahhalis.bookreadtracker.util.ViewModelFactory
import com.abdullahhalis.bookreadtracker.util.WANT_TO_READ_VALUE
import com.google.android.material.tabs.TabLayoutMediator

class StatusBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatusBookBinding
    private lateinit var adapter: StatusBookAdapter
    private val viewmodel: StatusBookViewModel by viewModels { ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStatusBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbarStatusBookActivity)
        supportActionBar?.setTitle(R.string.title_activity_status)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter = StatusBookAdapter { book ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(BOOK_ID, book.id)
            startActivity(intent)
        }
        binding.apply {
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                viewPager.setCurrentItem(viewmodel.tabPosition, true)
                tab.text = when (position) {
                    0 -> WANT_TO_READ_VALUE
                    1 -> CURRENTLY_READING_VALUE
                    2 -> FINISHED_READING_VALUE
                    else -> WANT_TO_READ_VALUE
                }
            }.attach()
        }
        viewmodel.booksStatusWantToRead.observe(this) { books ->
            books?.let {
                adapter.submitData(BookStatusType.WANT_TO_READ, books)
            }
        }
        viewmodel.booksStatusCurrentlyReading.observe(this) { books ->
            books?.let {
                adapter.submitData(BookStatusType.CURRENTLY_READING, books)
            }
        }
        viewmodel.booksStatusFinishedReading.observe(this) { books ->
            books?.let {
                adapter.submitData(BookStatusType.FINISHED_READING, books)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        viewmodel.tabPosition = binding.tabs.selectedTabPosition
    }
}