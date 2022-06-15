package com.example.githubapi

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter (private val list: ArrayList<UsersResponse>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onClickListener: View.OnClickListener

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(list[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(getResponse: UsersResponse) {
            with(itemView) {
                val text_name = "Name: ${getResponse.login}"
                val text_type = "Type: ${getResponse.type}"
                val tvText: TextView = findViewById(R.id.tvText)
                val tvType: TextView = findViewById(R.id.tvType)
                tvText.text = text_name
                tvType.text = text_type

                var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_avatar)
                val avatar = "${getResponse.avatarUrl}"
                Glide.with(this)
                    .load(avatar)
                    .circleCrop()
                    .into(imgPhoto)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UsersResponse)
    }
}