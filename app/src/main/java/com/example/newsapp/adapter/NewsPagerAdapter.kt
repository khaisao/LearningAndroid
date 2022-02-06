package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.MainActivity
import com.example.newsapp.fragments.BusinessFragment
import com.example.newsapp.fragments.HomeFragment
import com.example.newsapp.fragments.TechCrunchFragment
import com.example.newsapp.fragments.TeslaFragment
import java.lang.IllegalArgumentException

class NewsPagerAdapter(homeFragment: HomeFragment):FragmentStateAdapter(homeFragment) {
    private val tabs = arrayOf("Tesla", "TechCrunch", "Business")
    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TeslaFragment()
            1 -> TechCrunchFragment()
            2 -> BusinessFragment()
            else -> throw IllegalArgumentException("Unknow $position")
        }
    }
}