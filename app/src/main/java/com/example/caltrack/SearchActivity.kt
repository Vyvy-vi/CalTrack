package com.example.caltrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caltrack.api.ApiService
import com.example.caltrack.api.FoodItem
import com.example.caltrack.api.SearchResultClickListener
import com.example.caltrack.api.SearchResultsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity(), SearchResultClickListener {
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

        adapter = SearchResultsAdapter(searchResults, this)
        searchResultsList.adapter = adapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val query = searchBar.text.toString().trim()
                if (query.isNotEmpty()) {
                    fetchSearchResults(query)
                }
            }
        })
    }

    private fun fetchSearchResults(query: String) {
        // Use Coroutines for asynchronous network call
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("SearchActivity", "fetching results for query: $query")
            val response = try {
                ApiService._interface.search(query)
            } catch (e: Exception) {
                // Handle network exceptions (e.g., show a toast message)
                Log.e("SearchActivity", "Error fetching search results: ${e.message}")
                return@launch
            }

            Log.i("Search API", "Calling API")
            if (response.isSuccessful) {
                val searchResponse = response.body()
                Log.i("Search API", "Response " + searchResponse.toString())
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

    override fun onSearchResultClick(foodName: String, photoUrl: String) {
        // Handle click on a specific food item (e.g., navigate to a detail activity)
        val intent = Intent(this, FoodLogActivity::class.java)
        intent.putExtra("foodName", foodName)
        intent.putExtra("photoUrl", photoUrl)
        startActivity(intent)
    }

}