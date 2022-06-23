package com.example.githubapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserSearchAdapter (private val list:List<ItemsItem>, private var listener: OnItemClickListener): RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            listener.onItemClickUserSearch(list[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(getResponse: ItemsItem) {
            with(itemView) {
                val tvText: TextView = itemView.findViewById(R.id.tvText)
                val tvTextType: TextView = itemView.findViewById(R.id.tvType)
                val tvImages: ImageView = itemView.findViewById(R.id.img_item_avatar)
                val ava = "${getResponse.avatarUrl}"
                Glide.with(this)
                    .load(ava)
                    .circleCrop()
                    .into(tvImages)
                val textDataLogin = "${getResponse.login}"
                tvText.text = textDataLogin
                val textDataType = "${getResponse.type}"
                tvTextType.text = textDataType
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClickUserSearch(data: ItemsItem)
    }

}

