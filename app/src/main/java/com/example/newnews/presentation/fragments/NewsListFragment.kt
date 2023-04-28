package com.example.newnews.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.example.newnews.R
import com.example.newnews.databinding.FragmentNewsListBinding
import com.example.newnews.databinding.NewsActionbarBinding
import com.example.newnews.presentation.models.CategoryItem
import com.example.newnews.presentation.models.NewsItem
import com.example.newnews.presentation.fragments.viewmodels.NewsListViewModel
import com.xwray.groupie.GroupieAdapter

class NewsListFragment : Fragment(R.layout.fragment_news_list) {

    private lateinit var binding: FragmentNewsListBinding

    private lateinit var newsListViewModel: NewsListViewModel

    private var adapter = GroupieAdapter()
    private var categoryAdapter = GroupieAdapter()

    private var currentCategory = "technology"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewsListBinding.bind(view)

        setupNewsRecyclerView()
        setupCategoryRecyclerView()

        newsListViewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)

        binding.swipe.setOnRefreshListener {
            adapter.clear()
            newsListViewModel.getNewsData(currentCategory)
        }

        newsListViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipe.isRefreshing = it
            if (binding.swipe.isRefreshing) {
                binding.applicationNameText.visibility = View.VISIBLE
            }
            else {
                binding.applicationNameText.visibility = View.GONE
            }
        }

        newsListViewModel.newsLiveData.observe(viewLifecycleOwner) { news ->
            news.forEach {
                adapter.add(NewsItem(it))
            }
            binding.rvCategoryList.isClickable = true
        }

        newsListViewModel.categoryLiveData.observe(viewLifecycleOwner) { list ->
            list.forEach { category ->
                categoryAdapter.add(CategoryItem(category))
            }
        }
    } // end onViewCreated

    private fun setupNewsRecyclerView() {
        binding.rvNewsList.adapter = adapter
        binding.rvNewsList.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener { item, _ ->
            val newsItem = item as NewsItem

            val direction =
                NewsListFragmentDirections.actionNewsListFragmentToNewsFragment(newsItem.news)

            findNavController().navigate(direction, navOptions {
                anim {
                    enter = R.anim.from_right
                    exit = R.anim.to_left
                    popEnter = R.anim.from_left
                    popExit = R.anim.to_right
                }
            })

            adapter.clear()
            categoryAdapter.clear()
        }
    } // end setupNewsRecyclerView

    private fun setupCategoryRecyclerView() {
        binding.rvCategoryList.adapter = categoryAdapter

        binding.rvCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvCategoryList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL))

        categoryAdapter.setOnItemClickListener { item, _ ->
            if(binding.rvCategoryList.isClickable) {
                binding.rvCategoryList.isClickable = false
                adapter.clear()
                val categoryItem = item as CategoryItem
                currentCategory = categoryItem.category
                newsListViewModel.getNewsData(currentCategory)
                val actionBar_title = activity?.findViewById<TextView>(R.id.actionBar_title)
                actionBar_title?.text = "NewNews: $currentCategory"
            }
        }
    } // end setupCategoryRecyclerView
}