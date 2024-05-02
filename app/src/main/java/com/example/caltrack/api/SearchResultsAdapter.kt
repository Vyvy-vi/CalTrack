package com.example.caltrack.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caltrack.R

class SearchResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val foodIdTextView: TextView = itemView.findViewById(R.id.food_id)
    val foodNameTextView: TextView = itemView.findViewById(R.id.food_name)
}

class SearchResultsAdapter(
    private var items: List<FoodItem>,
    private val clickListener: SearchResultClickListener
) :
    RecyclerView.Adapter<SearchResultsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_result_item, parent, false)
        return SearchResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {
        val item = items[position]
        holder.foodIdTextView.text = (position + 1).toString() + "."
        holder.foodNameTextView.text = item.food_name

        holder.itemView.setOnClickListener {
            clickListener.onSearchResultClick(item.food_name, item.photo.thumb)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<FoodItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}