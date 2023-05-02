package com.example.newnews.presentation.fragments.newsfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newnews.R
import com.example.newnews.databinding.FragmentNewsBinding
import com.example.newnews.presentation.fragments.NewsFragmentArgs

class NewsFragment : Fragment(R.layout.fragment_news) {
    private lateinit var binding: FragmentNewsBinding

    private val args: NewsFragmentArgs by navArgs()

    // back button in actionBar
    private var actionBar_backButton: ImageButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        actionBar_backButton = activity?.findViewById<ImageButton>(R.id.actionBar_backButton)
        actionBar_backButton?.visibility = View.VISIBLE

        // callback for backPressed
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressed()
            }
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, callback)

        // setup back button in actionbar
        actionBar_backButton?.setOnClickListener {
            backPressed()
        }

        // receive argument from NewListFragment
        val news = args.news
        Glide.with(binding.root)
                .load(news.imageUrl)
                .into(binding.newsImage)

        binding.newsDate.text = news.date
        binding.newsAuthor.text = news.author

        binding.newsTitle.text = news.title
        binding.newsDescription.text = news.content
        binding.newsReadmoreLink.setOnClickListener {
            if (news.readMoreUrl != null) {
                val intentBrowser = Intent(Intent.ACTION_VIEW)
                intentBrowser.setData(Uri.parse(news.readMoreUrl))
                startActivity(intentBrowser)
            }
            else {
                Toast.makeText(requireContext(), "can't load this page", Toast.LENGTH_SHORT).show()
            }
        }
    } // end onViewCreated

    private fun backPressed(){
        findNavController().popBackStack()
        actionBar_backButton?.visibility = View.GONE
    } // end backPressed
}