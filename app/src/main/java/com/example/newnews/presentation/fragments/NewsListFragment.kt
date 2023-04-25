package com.example.newnews.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newnews.R
import com.example.newnews.data.factories.NewsApiFactory
import com.example.newnews.databinding.FragmentNewsListBinding
import com.example.newnews.presentation.models.NewsItem
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewsListFragment : Fragment(R.layout.fragment_news_list) {
    private lateinit var binding: FragmentNewsListBinding

    private var adapter = GroupieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsListBinding.bind(view)

        setupRecyclerView()

        GlobalScope.launch {
            val api = NewsApiFactory.newsApi

            val news = api.getNews("technology")
            activity?.runOnUiThread {
                news.data.forEach {
                    adapter.add(NewsItem(it))
                }
            }
        }

    }

    private fun setupRecyclerView(){
        binding.rvNewsList.adapter = adapter
        binding.rvNewsList.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnItemClickListener { item, view ->
            val newsItem = item as NewsItem

            val destination = NewsListFragmentDirections.actionNewsListFragmentToNewsFragment(newsItem.news)
            findNavController().navigate(destination)
        }
    }
}