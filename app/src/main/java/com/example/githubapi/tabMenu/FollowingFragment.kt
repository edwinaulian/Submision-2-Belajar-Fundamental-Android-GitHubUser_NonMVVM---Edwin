package com.example.githubapi.tabMenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.githubapi.ListFollowersResponseItem
import com.example.githubapi.R
import com.example.githubapi.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private val list = ArrayList<ListFollowersResponseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getString("key")
        if (user != null) {
            val client = RetrofitClient.getApiService().getFollowers(user)
            client.enqueue(object: Callback<ArrayList<ListFollowersResponseItem>> {
                override fun onResponse(call: Call<ArrayList<ListFollowersResponseItem>>, response: Response<ArrayList<ListFollowersResponseItem>>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            response.body()?.let { list.addAll(it) }
                            bindImageAva(view, responseBody)
                        }
                    } else {
                        Log.e(FollowingFragment.TAG,"onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<ListFollowersResponseItem>>, t: Throwable) {
                    Log.e(FollowingFragment.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun bindImageAva(view: View, data: ArrayList<ListFollowersResponseItem>?) {
        val imgPhoto  : ImageView = view.findViewById(R.id.img_item_avatar_detail_activity_following)
        val tvUserName: TextView = view.findViewById(R.id.tvUserNameListFollowing)
        val tvUserType: TextView = view.findViewById(R.id.tvTypeListFollowing)
        if (data != null) {
            for (item in data) {
                if (data != null) {
                    tvUserName.text = item.login
                    tvUserType.text = item.type
                    Glide.with(this@FollowingFragment)
                        .load(item.avatarUrl)
                        .circleCrop()
                        .into(imgPhoto)
                }
            }
        }
    }

    companion object {
        private const val TAG = "Following User"
    }
}