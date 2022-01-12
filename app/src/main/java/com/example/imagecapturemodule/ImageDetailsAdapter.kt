package com.example.imagecapturemodule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imagecapturemodule.database.ImageDetails

class ImageDetailsAdapter :
    ListAdapter<ImageDetails, ImageDetailsAdapter.ImageViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(" user id: "+current.userId+"\n question id: "+current.question+"\n answer marked: "+current.answer_marked+"\n image clicked"+current.image)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            wordItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): ImageViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ImageViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<ImageDetails>() {
            override fun areItemsTheSame(oldItem: ImageDetails, newItem: ImageDetails): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ImageDetails, newItem: ImageDetails): Boolean {
                return oldItem.question == newItem.question
            }
        }
    }
}
