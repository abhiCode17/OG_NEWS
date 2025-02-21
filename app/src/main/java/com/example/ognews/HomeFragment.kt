package com.example.ognews;

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.ognews.adapter.NewsAdapter
import com.app.ognews.model.Article
import com.app.ognews.model.NewsResponse
import com.app.ognews.retrofitapi.NewsApiService
import com.app.ognews.retrofitapi.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment: Fragment() {

    private val newsList = mutableListOf<Article>()
    private lateinit var adapter: NewsAdapter
    private lateinit var searchView: EditText
    private lateinit var categorySpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        searchView = view.findViewById(R.id.searchView)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        adapter = NewsAdapter(requireContext(), newsList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter // Set adapter

        fetchNewsByQuery("top-headlines")
        setUpSearchView()
        setUpCategorySpinner()

        return view
    }

    private fun fetchNewsByQuery(query: String) {
        RetrofitClient.instance.searchNews(query).enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    newsList.clear()
                    newsList.addAll(response.body()!!.articles)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT).show()
            }

        }

        )
    }

    private fun setUpCategorySpinner() {
        val categories = listOf("Select Category.....", "General", "Business", "Technology", "Entertainment", "Health", "Science")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        categorySpinner.adapter = adapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position].lowercase()
                fetchNewsByCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun fetchNewsByCategory(category: String) {
        if (category == "Select Category") {
            fetchNewsByQuery("top-headlines") // Load homepage if no category selected
            return
        }

        RetrofitClient.instance.searchNews(category).enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("API_SUCCESS", "Fetched news for category: $category")
                    newsList.clear()
                    newsList.addAll(response.body()!!.articles)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("API_ERROR", "Response failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failed to fetch news", t)
                Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun setUpSearchView() {
        searchView.setOnFocusChangeListener { view, hasFocus ->
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if (hasFocus) {

                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

                searchView.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val query = s.toString().trim()
                        if (query.isNotEmpty()) {
                            fetchNewsByQuery(query)
                        }
                    }
                })
            } else {

                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                searchView.clearFocus()
            }
        }
    }


//    private fun setUpSearchView() {
//        searchView.setOnFocusChangeListener(object: SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if(!query.isNullOrEmpty()) {
//                    fetchNewsByQuery(query)
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//
//        })
//    }




}
