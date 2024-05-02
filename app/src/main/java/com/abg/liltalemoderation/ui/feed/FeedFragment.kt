package com.abg.liltalemoderation.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.repository.ComplaintRepository
import com.abg.liltalemoderation.databinding.FragmentFeedBinding
import com.abg.liltalemoderation.ui.AudioBaseFragment
import com.abg.liltalemoderation.ui.ReportAdapter
import com.abg.liltalemoderation.ui.ReportViewModel
import com.abg.liltalemoderation.ui.ReportViewModelFactory

class FeedFragment : AudioBaseFragment(){

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReportAdapter
    private val reportViewModel: ReportViewModel by activityViewModels {
        ReportViewModelFactory(ComplaintRepository(RemoteInstance))
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

        adapter = ReportAdapter(load, prepareAudioHandler())
        recyclerView.adapter = adapter

        reportViewModel.reports.observe(viewLifecycleOwner) {
            adapter.setPosts(it)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player.stop()
    }

}