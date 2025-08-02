package com.abdullahhalis.bookreadtracker.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.ActivityMainBinding
import com.abdullahhalis.bookreadtracker.ui.detail.DetailActivity
import com.abdullahhalis.bookreadtracker.ui.list.ListBookActivity
import com.abdullahhalis.bookreadtracker.ui.status.StatusBookActivity
import com.abdullahhalis.bookreadtracker.util.BOOK_ID
import com.abdullahhalis.bookreadtracker.util.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            customCurrentlyReadBookView.setAdapterItemClickCallback { book: Book ->
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(BOOK_ID, book.id)
                startActivity(intent)
            }

            viewModel.listOfCurrentlyReadBook.observe(this@MainActivity) { books ->
                customCurrentlyReadBookView.submitData(books)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_status -> {
                val intent = Intent(this, StatusBookActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_list -> {
                val intent = Intent(this, ListBookActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}