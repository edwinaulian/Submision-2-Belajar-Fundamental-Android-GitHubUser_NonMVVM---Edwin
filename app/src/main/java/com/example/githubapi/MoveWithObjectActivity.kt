package com.example.githubapi

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapi.tabMenu.FollowersFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoveWithObjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_object_detailuser)

        val user = intent.getStringExtra(EXTRA_USER).toString()

        val bundle = Bundle()
        bundle.putString("key", user) // Put anything what you want

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragment1 = FollowersFragment()
        fragment1.arguments = bundle
        transaction.replace(R.id.relativeLayout, fragment1)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position -> tab.text = resources.getString(TAB_TITLES[position]) }.attach()
        supportActionBar?.elevation = 0f
        if (user != null) {
            getDetailUser(user)
        }
    }

    private fun getDetailUser(user: String) {
        val client =RetrofitClient.getApiService().getDetailUser(user)
        client.enqueue(
            object: Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            bindImageAva(responseBody)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        )
    }

    private fun bindImageAva(data: UserDetailResponse?){
        var imgPhoto: ImageView = findViewById(R.id.img_item_avatar_detail_activity)
        val followers: TextView = findViewById(R.id.tvFollowers)
        val following: TextView = findViewById(R.id.tvFollowing)
        if (data != null) {
            Glide.with(this)
                .load(data.avatarUrl)
                .circleCrop()
                .into(imgPhoto)
            val countFollowers = data.followers
            val countFollowing = data.following
            followers.text = countFollowers.toString()
            following.text = countFollowing.toString()
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private const val TAG = "Detail Activity User"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
