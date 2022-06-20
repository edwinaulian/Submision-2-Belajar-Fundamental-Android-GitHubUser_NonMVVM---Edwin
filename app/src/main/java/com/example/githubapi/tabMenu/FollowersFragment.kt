package com.example.githubapi.tabMenu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {

    private val list = ArrayList<ListFollowersResponseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getListFollowers(user: String, view: View) {
        val client = RetrofitClient.getApiService().getFollowers(user)
        client.enqueue(
            object: Callback<ArrayList<ListFollowersResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<ListFollowersResponseItem>>,
                    response: Response<ArrayList<ListFollowersResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            response.body()?.let { list.addAll(it) }
                            val adapter = UserFollowersFollowingAdapter(list, this@FollowersFragment)
                            val rvUser: RecyclerView = view.findViewById(R.id.rvUserFollowers)
                            rvUser.adapter = adapter

                            //bindImageAva(view, responseBody)
//                            view?.let { bindImageAva(it, responseBody) }
                        }
                    } else {
                        Log.e(TAG,"onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ListFollowersResponseItem>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
    }

    private fun bindImageAva(view: View, data: ArrayList<ListFollowersResponseItem>?) {
//        val imgPhoto  : ImageView = view.findViewById(R.id.img_item_avatar_detail_activity_followers)
        val tvUserName: TextView = view.findViewById(R.id.tvUserNameListFollowers)
        val tvUserType: TextView = view.findViewById(R.id.tvTypeListFollowers)
        Log.d("tay", "${data}")
        if (data != null) {
            for (item in data) {
                if (data != null) {
                    tvUserName.text = item.login
                    tvUserType.text = item.type
//                    Glide.with(this@FollowersFragment)
//                        .load(item.avatarUrl)
//                        .circleCrop()
//                        .into(imgPhoto)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getString("key")
        if (user != null) {
//            view?.let { getListFollowers(user, it) }
            val client = RetrofitClient.getApiService().getFollowers(user)
            client.enqueue(
                object: Callback<ArrayList<ListFollowersResponseItem>> {
                    override fun onResponse(
                        call: Call<ArrayList<ListFollowersResponseItem>>,
                        response: Response<ArrayList<ListFollowersResponseItem>>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                response.body()?.let { list.addAll(it) }
                                val adapter = UserFollowersFollowingAdapter(list, this@FollowersFragment)
                                val rvUser: RecyclerView = view.findViewById(R.id.rvUserFollowers)
                                rvUser.adapter = adapter
                                Log.d(TAG, "halo edwin${list}")
                                //bindImageAva(view, responseBody)
//                            view?.let { bindImageAva(it, responseBody) }
                            }
                        } else {
                            Log.e(TAG,"onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<ArrayList<ListFollowersResponseItem>>,
                        t: Throwable
                    ) {
                        Log.e(TAG, "onFailure: ${t.message}")
                    }
                })

            Log.d("sukses dunia akhirat", "${user}")
        }
    }

    companion object {
        private const val TAG = "Followers User"
    }
}