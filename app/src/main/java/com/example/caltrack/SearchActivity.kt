package com.example.caltrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caltrack.api.ApiService
import com.example.caltrack.api.FoodItem
import com.example.caltrack.api.SearchResultsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var searchBar: EditText
    private lateinit var searchResultsList: RecyclerView
    private lateinit var adapter: SearchResultsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var searchResults: List<FoodItem> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        searchBar = findViewById(R.id.search_bar)
        searchResultsList = findViewById(R.id.search_results_list)

        layoutManager = LinearLayoutManager(this)
        searchResultsList.layoutManager = layoutManager

        adapter = SearchResultsAdapter(searchResults)
        searchResultsList.adapter = adapter

        searchBar.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    val query = searchBar.text.toString().trim()
                    if (query.isNotEmpty()) {
                        fetchSearchResults(query)
                    }
                }
                return false
            }
        })
    }
    private fun fetchSearchResults(query: String) {
        // Use Coroutines for asynchronous network call
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                ApiService._interface.search(query)
            } catch (e: Exception) {
                // Handle network exceptions (e.g., show a toast message)
                Log.e("SearchActivity", "Error fetching search results: ${e.message}")
                return@launch
            }

            if (response.isSuccessful) {
                val searchResponse = response.body()
                if (searchResponse != null) {
                    searchResults = searchResponse.common
                    runOnUiThread {
                        adapter.updateItems(searchResults) // Update adapter with new data
                    }
                } else {
                    // Handle empty response (e.g., show a message indicating no results found)
                    Log.d("SearchActivity", "Empty search response")
                }
            } else {
                // Handle unsuccessful response (e.g., show an error message)
                val errorBody = response.errorBody()
                val errorString = errorBody?.string() ?: "Unknown error"
                Log.e("SearchActivity", "Error fetching search results: $errorString")
            }
        }
    }

}