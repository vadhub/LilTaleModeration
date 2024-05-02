package com.abg.liltalemoderation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.abg.liltalemoderation.databinding.ItemPostBinding
import com.abg.liltalemoderation.model.PlaylistHandler
import com.abg.liltalemoderation.model.pojo.ComplaintReport
import com.abg.liltalemoderation.ui.FileViewModel
import java.text.SimpleDateFormat

class ReportAdapter(
    private val load: FileViewModel,
    private val playlistHandler: PlaylistHandler,
    private val leavePost: (Long) -> Unit,
    private val deletePost: (Long, Long) -> Unit
) : RecyclerView.Adapter<ReportAdapter.PostViewHolder>() {

    private var reports: List<ComplaintReport> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setPosts(reports: List<ComplaintReport>) {
        if (reports.isNotEmpty()) {
            this.reports = reports
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = reports[position]
        holder.bind(post)
    }

    override fun getItemCount() = reports.size

    inner class PostViewHolder(itemView: ItemPostBinding) : ViewHolder(itemView.root) {
        private val textViewDate = itemView.textViewDate
        private val imageViewPost = itemView.imageViewPost
        private val recyclerViewAudio = itemView.audioRecycler
        private lateinit var adapter: AudioAdapter
        private val imageIcon = itemView.imageIconPost
        private val hashtag = itemView.textViewHashtag
        private val nikName = itemView.textViewNikName
        private val removeComplaint = itemView.removeComplaint
        private val removePost = itemView.removePost

        @SuppressLint("SimpleDateFormat")
        fun bind(report: ComplaintReport) {

            imageIcon.setImageDrawable(null)
            imageViewPost.setImageDrawable(null)
            nikName.text = ""
            hashtag.text = ""
            textViewDate.text = ""

            removeComplaint.setOnClickListener { leavePost.invoke(report.id) }
            removePost.setOnClickListener { deletePost.invoke(report.post.postId, report.id) }

            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd.MM.yyyy")

            textViewDate.text = formatter.format(parser.parse(report.dateCreated)?: "0.0.0") // if data is null return 0.0.0
            nikName.text = report.post.nikName
            load.getIcon(report.post.userId, imageIcon)
            load.getImage(report.post.image?.id, imageViewPost)

            if (report.post.hashtags.isNotEmpty()) hashtag.text = report.post.hashtags.map { it.hashtagName }.reduce { acc, s -> "$acc $s" }

            recyclerViewAudio.layoutManager = LinearLayoutManager(itemView.context)
            adapter = AudioAdapter(layoutPosition, playlistHandler)
            adapter.setRecords(report.post.listAudio)
            recyclerViewAudio.adapter = adapter
        }
    }

}
