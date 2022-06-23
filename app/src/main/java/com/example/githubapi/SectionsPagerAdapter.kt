package com.example.githubapi

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubapi.tabMenu.FollowersFragment
import com.example.githubapi.tabMenu.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, user: String) : FragmentStateAdapter(activity) {

    val user = user

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment(user)
            1 -> fragment = FollowingFragment(user)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return  2
    }

}