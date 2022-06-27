package com.example.githubapi.tabMenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment () : Fragment() {

    private val list = ArrayList<ListFollowersResponseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            val user = ""
            val myUserDetail = bundle.getString("key", user)
            USER = myUserDetail
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoading(view, true)
        super.onViewCreated(view, savedInstanceState)
        val activity: MoveWithObjectActivity? = activity as MoveWithObjectActivity?
        activity.toString()
        val user = USER
        if (user != null) {
            val client = RetrofitClient.repositorioRetrofit().getFollowing(user)
            client.enqueue(object: Callback<ArrayList<ListFollowersResponseItem>> {
                override fun onResponse(call: Call<ArrayList<ListFollowersResponseItem>>, response: Response<ArrayList<ListFollowersResponseItem>>) {
                    if (response.isSuccessful) {
                        showLoading(view, false)
                        val responseBody = response.body()
                        if (responseBody != null) {
                            response.body()?.let { list.addAll(it) }
                            val adapter = UserFollowingAdapter(list, this@FollowingFragment)
                            val rvUser: RecyclerView = view.findViewById(R.id.rvUser)
                            rvUser.layoutManager = LinearLayoutManager(view.context)
                            rvUser.adapter = adapter
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

    private fun showLoading(view: View, isLoading: Boolean) {
        val binding : ProgressBar = view.findViewById(R.id.progressBar)
        if (isLoading) {
            binding.visibility = View.VISIBLE
        } else {
            binding.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "Following User"
        private var USER = ""
    }
}