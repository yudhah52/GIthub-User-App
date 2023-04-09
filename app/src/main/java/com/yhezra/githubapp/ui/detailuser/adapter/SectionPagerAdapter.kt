package com.yhezra.githubapp.ui.detailuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yhezra.githubapp.ui.detailuser.FollowersFragment
import com.yhezra.githubapp.ui.detailuser.FollowingFragment

class SectionPagerAdapter(activity:AppCompatActivity):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    var username: String? = null

    override fun createFragment(position: Int): Fragment {
        var fragment:Fragment?=null
        when(position){
            0->fragment=FollowersFragment.newInstance(username.toString())
            1->fragment = FollowingFragment.newInstance(username.toString())
        }
        return fragment as Fragment
    }
}