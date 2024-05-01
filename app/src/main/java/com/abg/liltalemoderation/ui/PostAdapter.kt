package com.abg.liltalemoderation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.abg.liltalemoderation.databinding.ItemPostBinding
import com.abg.liltalemoderation.model.PlaylistHandler
import com.abg.liltalemoderation.model.pojo.PostResponse
import java.text.SimpleDateFormat

class PostAdapter(
    private val load: FileViewModel,
    private val playlistHandler: PlaylistHandler
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var posts: List<PostResponse> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setPosts(posts: List<PostResponse>) {
        if (posts.isNotEmpty()) {
            this.posts = posts
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount() = posts.size

    inner class PostViewHolder(itemView: ItemPostBinding) : ViewHolder(itemView.root) {
        private val textViewDate = itemView.textViewDate
        private val imageViewPost = itemView.imageViewPost
        private val recyclerViewAudio = itemView.audioRecycler
        private lateinit var adapter: AudioAdapter
        private val imageIcon = itemView.imageIconPost
        private val hashtag = itemView.textViewHashtag
        private val nikName = itemView.textViewNikName
        private var postResponse = PostResponse.empty()

        @SuppressLint("SimpleDateFormat")
        fun bind(postResponse: PostResponse) {
            this.postResponse = postResponse

            imageIcon.setImageDrawable(null)
            imageViewPost.setImageDrawable(null)
            nikName.text = ""
            hashtag.text = ""
            textViewDate.text = ""

            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd.MM.yyyy")

            textViewDate.text = formatter.format(parser.parse(postResponse.dateChanged)?: "0.0.0") // if data is null return 0.0.0
            nikName.text = postResponse.nikName
            load.getIcon(postResponse.userId, imageIcon)
            load.getImage(postResponse.image?.id, imageViewPost)

            if (postResponse.hashtags.isNotEmpty()) hashtag.text = postResponse.hashtags.map { it.hashtagName }.reduce { acc, s -> "$acc $s" }

            recyclerViewAudio.layoutManager = LinearLayoutManager(itemView.context)
            adapter = AudioAdapter(layoutPosition, playlistHandler, false)
            adapter.setRecords(postResponse.listAudio)
            recyclerViewAudio.adapter = adapter
        }
    }

}
