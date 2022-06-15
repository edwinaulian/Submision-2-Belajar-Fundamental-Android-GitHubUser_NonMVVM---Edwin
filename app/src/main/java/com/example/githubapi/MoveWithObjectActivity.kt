package com.example.githubapi

import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubedwin.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MoveWithObjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_object_detailuser)

        val user = intent.getStringExtra(EXTRA_USER) as UsersResponse
        var imgPhoto: ImageView = findViewById(R.id.img_item_avatar_detail)
        val picAvatar = user.avatarUrl;
        Glide.with(this)
            .load(picAvatar)
            .circleCrop()
            .into(imgPhoto)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val  tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position -> tab.text = resources.getString(TAB_TITLES[position]) }.attach()
        supportActionBar?.elevation = 0f
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
