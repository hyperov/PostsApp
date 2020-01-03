package com.nabil.postsapp.posts.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nabil.postsapp.R
import com.nabil.postsapp.databinding.ItemPostBinding
import com.nabil.postsapp.posts.model.pojo.Post

class PostsAdapter(
    private var data: ArrayList<Post> = ArrayList(),
    private val onClick: (post: Post) -> Unit,
    private val onEditPost: (post: Post, position: Int) -> Unit,
    private val onDeletePost: (post: Post, position: Int) -> Unit
) :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val postItemBinding = DataBindingUtil.inflate<ItemPostBinding>(
            layoutInflater,
            R.layout.item_post, parent, false
        )

        return PostsViewHolder(postItemBinding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) =
        holder.bind(data[position])

    fun swapData(data: ArrayList<Post>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addPost(post: Post) {
        this.data.add(post)
        notifyItemInserted(itemCount)
    }

    inner class PostsViewHolder(var itemViewBinding: ItemPostBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(item: Post) = run {
            itemViewBinding.apply {

                post = item
                root.setOnClickListener { onClick(item) }
                btEdit.setOnClickListener { onEditPost(item, adapterPosition) }
                btDelete.setOnClickListener { onDeletePost(item, adapterPosition) }
                return@run
            }
        }
    }
}