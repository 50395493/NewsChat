package com.stealthnews.chat.ui.news

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stealthnews.chat.ui.news.fragment.TechNewsFragment
import com.stealthnews.chat.ui.news.fragment.SportsNewsFragment
import com.stealthnews.chat.ui.news.fragment.EntertainmentNewsFragment

class NewsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    
    private val fragments = listOf(
        TechNewsFragment(),
        SportsNewsFragment(),
        EntertainmentNewsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}
