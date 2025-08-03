package com.abdullahhalis.bookreadtracker.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abdullahhalis.bookreadtracker.R
import com.abdullahhalis.bookreadtracker.data.Book
import com.abdullahhalis.bookreadtracker.databinding.ActivityAddBookBinding
import com.abdullahhalis.bookreadtracker.util.BookStatusType
import com.abdullahhalis.bookreadtracker.util.ViewModelFactory
import com.abdullahhalis.bookreadtracker.util.toBookStatusType

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private val viewModel: AddBookViewModel by viewModels { ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbarAddBookActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.saved.observe(this) { event ->
            val saved = event.getContentIfNotHandled()
            if (saved == true) {
                Toast.makeText(this, getString(R.string.success_added_book), Toast.LENGTH_SHORT).show()

                binding.apply {
                    tietTitleAdd.text?.clear()
                    tietGenreAdd.text?.clear()
                    tietTotalPageAdd.text?.clear()
                    tietAuthorAdd.text?.clear()
                    spinnerStatusAdd.setSelection(0)
                    tietReadingProgressAdd.text?.clear()
                    tietPersonalNoteAdd.text?.clear()

                    vgAddBookActivity.clearFocus()
                }
            }
        }

        binding.spinnerStatusAdd.apply {
            val listOfBookStatus = BookStatusType.entries.map { it.value }
            adapter = ArrayAdapter(
                this@AddBookActivity,
                android.R.layout.simple_list_item_1,
                listOfBookStatus
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when(position) {
                        1 -> {
                            binding.tilReadingProgressAdd.visibility = View.VISIBLE
                            binding.tilPersonalNoteAdd.visibility = View.GONE
                        }
                        2 -> {
                            binding.tilReadingProgressAdd.visibility = View.GONE
                            binding.tilPersonalNoteAdd.visibility = View.VISIBLE
                        }
                        else -> {
                            binding.tilReadingProgressAdd.visibility = View.GONE
                            binding.tilPersonalNoteAdd.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                binding.apply {
                    if (isFieldInputFilled()) {
                        val status = binding.spinnerStatusAdd.selectedItem.toString().toBookStatusType()
                        val totalPage = tietTotalPageAdd.text.toString().toIntOrNull() ?: 0
                        val readingProgress = when(status) {
                            BookStatusType.WANT_TO_READ -> 0
                            BookStatusType.FINISHED_READING -> totalPage
                            BookStatusType.CURRENTLY_READING -> tietReadingProgressAdd.text.toString().toIntOrNull() ?: 0
                        }
                        if (readingProgress > totalPage) {
                            Toast.makeText(
                                this@AddBookActivity,
                                getString(R.string.invalid_reading_progress_input_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.insertBook(Book(
                                title = tietTitleAdd.text.toString(),
                                genre = tietGenreAdd.text.toString(),
                                totalPage = totalPage,
                                author = tietAuthorAdd.text.toString(),
                                status = status.value,
                                readingProgress = readingProgress,
                                personalNote = tietPersonalNoteAdd.text.toString(),
                                bookAddedInMillis = System.currentTimeMillis()
                            ))
                        }
                    } else {
                        Toast.makeText(
                            this@AddBookActivity,
                            getString(R.string.invalid_input_data_not_filled_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

    private fun isFieldInputFilled(): Boolean {
        binding.apply {
            val checkTitleField = tietTitleAdd.text?.isBlank() == false
            val checkGenreField = tietGenreAdd.text?.isBlank() == false
            val checkTotalPageField = tietTotalPageAdd.text?.isBlank() == false
            val checkAuthorField = tietAuthorAdd.text?.isBlank() == false

            return checkTitleField && checkGenreField && checkTotalPageField && checkAuthorField
        }
    }
}