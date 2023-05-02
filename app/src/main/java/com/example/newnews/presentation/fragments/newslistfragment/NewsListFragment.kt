package com.example.newnews.presentation.fragments.newslistfragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newnews.R
import com.example.newnews.databinding.FragmentNewsListBinding
import com.example.newnews.presentation.fragments.NewsListFragmentDirections
import com.example.newnews.presentation.models.CategoryItem
import com.example.newnews.presentation.models.NewsItem
import com.xwray.groupie.GroupieAdapter

class NewsListFragment : Fragment(R.layout.fragment_news_list) {

    private lateinit var binding: FragmentNewsListBinding

    private lateinit var newsListViewModel: NewsListViewModel

    // adapters for recyclerViews
    private var newsListAdapter = GroupieAdapter()
    private var newsCategoryAdapter = GroupieAdapter()

    // save current category
    private var currentCategory = "technology"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewsListBinding.bind(view)

        // setup recycler views for news list and category list
        setupNewsRecyclerView()
        setupCategoryRecyclerView()

        // create viewModel instance
        newsListViewModel = ViewModelProvider(this).get(NewsListViewModel::class.java)

        // refresh page
        binding.swipe.setOnRefreshListener {
            newsListAdapter.clear()
            newsListViewModel.getNewsData(currentCategory)
        }

        // observe liveData fom NewsListViewModel
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
                newsListAdapter.add(NewsItem(it))
            }
            binding.rvCategoryList.isClickable = true
        }

        newsListViewModel.categoryLiveData.observe(viewLifecycleOwner) { list ->
            list.forEach { category ->
                newsCategoryAdapter.add(CategoryItem(category))
            }
        }
    } // end onViewCreated

    private fun setupNewsRecyclerView() {
        binding.rvNewsList.adapter = newsListAdapter
        binding.rvNewsList.layoutManager = LinearLayoutManager(requireContext())

        newsListAdapter.setOnItemClickListener { item, _ ->
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

            newsListAdapter.clear()
            newsCategoryAdapter.clear()
        }
    } // end setupNewsRecyclerView

    private fun setupCategoryRecyclerView() {
        binding.rvCategoryList.adapter = newsCategoryAdapter

        binding.rvCategoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvCategoryList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
                                 )
                                                )

        newsCategoryAdapter.setOnItemClickListener { item, _ ->
            if(binding.rvCategoryList.isClickable) {
                binding.rvCategoryList.isClickable = false
                newsListAdapter.clear()
                val categoryItem = item as CategoryItem
                currentCategory = categoryItem.category
                newsListViewModel.getNewsData(currentCategory)
                val actionBar_title = activity?.findViewById<TextView>(R.id.actionBar_title)
                actionBar_title?.text = "NewNews: $currentCategory"
            }
        }
    } // end setupCategoryRecyclerView
}