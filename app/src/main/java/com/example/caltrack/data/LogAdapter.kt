package com.example.caltrack.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caltrack.R


internal class WaterLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView
    var qty: TextView

    init {
        name = itemView.findViewById<View>(R.id.item_name) as TextView
        qty = itemView.findViewById<View>(R.id.item_quantity) as TextView
    }
}


internal class FoodLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView
    var qty: TextView

    init {
        name = itemView.findViewById<View>(R.id.item_name) as TextView
        qty = itemView.findViewById<View>(R.id.item_quantity) as TextView
    }
}

class LogAdapter(private var items: List<LogEntry>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> WaterLogViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.water_log_item, parent, false)
            )

            else -> FoodLogViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.food_log_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (item is WaterLogItem) {
            val waterHolder: WaterLogViewHolder = holder as WaterLogViewHolder
            waterHolder.name.text = "Water"
            waterHolder.qty.text = item.quantity.toString() + "L"
        } else if (item is FoodLogItem) {
            val foodHolder: FoodLogViewHolder = holder as FoodLogViewHolder
            foodHolder.name.text = item.foodname
            foodHolder.qty.text = item.quantity.toString() + "g"
        }
    }

    override fun getItemCount(): Int = items.size


    override fun getItemViewType(position: Int): Int {
        if (items[position] is WaterLogItem) {
            return 0
        } else {
            return 1
        }
    }

    fun updateItems(newItems: List<LogEntry>) {
        items = newItems
        notifyDataSetChanged()
    }
}