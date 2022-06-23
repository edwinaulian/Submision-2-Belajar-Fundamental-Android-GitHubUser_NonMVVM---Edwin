package com.example.githubapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.tabMenu.FollowingFragment

class UserFollowingAdapter(private val list: ArrayList<ListFollowersResponseItem>, followingFragment: FollowingFragment): RecyclerView.Adapter<UserFollowingAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_following, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(getResponse: ListFollowersResponseItem) {
            with(itemView) {
                // get value
                val text_name = "Name: ${getResponse.login}"
                val text_type = "Type: ${getResponse.type}"
                val src_avatar = "${getResponse.avatarUrl}"

                // get Source Id TextView
                val tvText_Login: TextView = findViewById(R.id.tvUserNameListFollowing)
                val tvText_Type: TextView = findViewById(R.id.tvTypeListFollowing)
                val tvImg: ImageView = itemView.findViewById(R.id.img_item_avatar_detail_activity_following)

                // Passing data
                tvText_Login.text = text_name
                tvText_Type.text = text_type
                Glide.with(this)
                    .load(src_avatar)
                    .circleCrop()
                    .into(tvImg)
            }
        }
    }
}