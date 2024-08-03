package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private var searchInputTextUser = ""
    private lateinit var searchInput: EditText
    private lateinit var searchInputClearButton: ImageView
    private val searchInputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchInputClearButton.visibility = clearButtonVisibility(s)
            searchInputTextUser = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchInput = findViewById(R.id.input_search_form)
        searchInput.requestFocus()
        searchInput.addTextChangedListener(searchInputTextWatcher)

        searchInputClearButton = findViewById(R.id.button_clear_search_form)
        searchInputClearButton.visibility = clearButtonVisibility(searchInput.text)
        searchInputClearButton.setOnClickListener {
            clearSearchForm()
        }

        findViewById<ImageView>(R.id.arrow_back_search).setOnClickListener {
            finish()
        }
    }

    private fun clearSearchForm() {
        searchInput.text.clear()

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputTextUser)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputTextUser = savedInstanceState.getString(SEARCH_QUERY, "")
        searchInput.setText(searchInputTextUser)
    }
}