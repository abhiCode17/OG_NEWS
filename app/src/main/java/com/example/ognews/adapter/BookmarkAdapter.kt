package com.app.ognews.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ognews.model.Article
import com.bumptech.glide.Glide
import com.example.ognews.R
import java.io.File

class BookmarkAdapter(
    private val context: Context,
    private val bookmarkedArticles: List<Article>,
    private val onRemoveBookmark: (Article) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val article = bookmarkedArticles[position]

        holder.newsTitle.text = article.title
        holder.newsDescription.text = article.description

        if (!article.imagePath.isNullOrEmpty()) {
            Glide.with(context)
                .load(File(article.imagePath))
                .into(holder.newsImage)
        } else {
            Glide.with(context)
                .load(article.urlToImage)
                .into(holder.newsImage)
        }

        holder.bookmarkButton.setOnClickListener {
            onRemoveBookmark(article)
        }
    }

    override fun getItemCount(): Int = bookmarkedArticles.size

    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val newsDescription: TextView = itemView.findViewById(R.id.newsDescription)
        val bookmarkButton: ImageView = itemView.findViewById(R.id.bookmarkButton)
    }
}
