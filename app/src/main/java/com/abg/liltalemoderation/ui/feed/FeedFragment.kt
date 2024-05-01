package com.abg.liltalemoderation.ui.feed

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abg.liltalemoderation.databinding.FragmentFeedBinding
import com.abg.liltalemoderation.ui.AudioBaseFragment
import com.abg.liltalemoderation.ui.PostAdapter
import com.abg.liltalemoderation.R

class FeedFragment : AudioBaseFragment(){

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progressBar: ProgressBar = binding.progressBarFeed
        val recyclerView: RecyclerView = binding.feedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(thisContext)

        adapter = PostAdapter(load, prepareAudioHandler())
        recyclerView.adapter = adapter

        postViewModel.posts.observe(viewLifecycleOwner) {
            adapter.setPosts(it)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_search_view, menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        val searchText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(Color.WHITE)

        searchView.isIconified = true
        searchView.queryHint = getString(R.string.searching)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    postViewModel.getPostsByText(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player.stop()
    }

}