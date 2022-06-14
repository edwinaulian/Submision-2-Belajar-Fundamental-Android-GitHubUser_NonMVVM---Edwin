package com.example.githubapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserSearchAdapter (private val list:List<ItemsItem>): RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        UserViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvText: TextView = itemView.findViewById(R.id.tvText)
        val tvTextType: TextView = itemView.findViewById(R.id.tvType)
        val tvImages: ImageView = itemView.findViewById(R.id.img_item_avatar)
        fun bind(getResponse: ItemsItem) {
            with(itemView) {
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

}

