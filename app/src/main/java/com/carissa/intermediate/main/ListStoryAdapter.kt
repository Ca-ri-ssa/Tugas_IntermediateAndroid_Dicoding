package com.carissa.intermediate.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissa.intermediate.data.response.ListStoryItem
import com.carissa.intermediate.databinding.StoryRowBinding
import com.carissa.intermediate.detail.DetailActivity
import com.carissa.intermediate.util.TimePost


class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.MainViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class MainViewHolder(private val binding: StoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, storyItem: ListStoryItem) {
            binding.root.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("detail_story", storyItem.id)
                context.startActivity(intent)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(storyItem.photoUrl)
                    .into(ivItemPhoto)
                tvItemUser.text = storyItem.name
                tvUserCaption.text = storyItem.description
                val formattedDate = TimePost.formatDatabaseDate(storyItem.createdAt, "dd MMM yyyy HH:mm:ss")
                tvTimePost.text = formattedDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = StoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(holder.itemView.context, story)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(storyItem: ListStoryItem)
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
