package com.example.newnews.presentation.models

import android.view.View
import com.bumptech.glide.Glide
import com.example.newnews.R
import com.example.newnews.data.models.News
import com.example.newnews.databinding.ItemNewsBinding
import com.xwray.groupie.viewbinding.BindableItem

class NewsItem(val news: News): BindableItem<ItemNewsBinding>() {
    override fun bind(viewBinding: ItemNewsBinding, position: Int) {
        val imageView = viewBinding.image
        Glide.with(imageView.context)
                .load(news.imageUrl)
                .into(imageView)

        viewBinding.author.text = news.author
        viewBinding.title.text = news.title
        viewBinding.date.text = news.date
    }

    override fun getLayout(): Int {
        return R.layout.item_news
    }

    override fun initializeViewBinding(view: View): ItemNewsBinding {
        return ItemNewsBinding.bind(view)
    }
}