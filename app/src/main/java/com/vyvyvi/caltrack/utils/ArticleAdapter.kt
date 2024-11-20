package com.vyvyvi.caltrack.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vyvyvi.caltrack.Article
import com.vyvyvi.caltrack.R

class ArticleAdapter(private val context: Context, private val articles: Array<Article>) :
    ArrayAdapter<Article>(context, R.layout.article_list_item, articles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.article_list_item, parent, false)

        val article = getItem(position)
        val titleTextView = view.findViewById<TextView>(R.id.articleTitle)
        val linkTextView = view.findViewById<TextView>(R.id.articleLink)

        titleTextView.text = article?.title
        linkTextView.text = article?.link

        return view
    }
}