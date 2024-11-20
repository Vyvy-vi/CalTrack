package com.vyvyvi.caltrack.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vyvyvi.caltrack.AlertsActivity
import com.vyvyvi.caltrack.CommunityActivity
import com.vyvyvi.caltrack.DashboardActivity
import com.vyvyvi.caltrack.LogActivity
import com.vyvyvi.caltrack.R
import com.vyvyvi.caltrack.SearchActivity
import com.vyvyvi.caltrack.StepsActivity
import com.vyvyvi.caltrack.WaterLogActivity

data class MenuOption(val title: String, val img: Int, val activity: Class<*>)
val items = arrayOf(
    MenuOption("Log Food", R.drawable.ic_log, SearchActivity::class.java),
    MenuOption("Log Water", R.drawable.ic_waterlog, WaterLogActivity::class.java),
    MenuOption("Log Steps", R.drawable.ic_steps, StepsActivity::class.java),
    MenuOption("History", R.drawable.ic_history, LogActivity::class.java),
    MenuOption("Alerts", R.drawable.ic_alerts, AlertsActivity::class.java),
    MenuOption("Community", R.drawable.ic_community, CommunityActivity::class.java),
)

class MenuGridAdapter(context : Context) :
    ArrayAdapter<MenuOption>(context,0, items) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = layoutInflater.inflate(R.layout.card_layout,null)
        val titleText = view.findViewById<TextView>(R.id.card_text)
        val imageView = view.findViewById<ImageView>(R.id.card_image)

        val mItem = items[position]

        titleText.text = mItem.title
        imageView.setImageDrawable(context.resources.getDrawable(mItem.img))
        return view
    }
}