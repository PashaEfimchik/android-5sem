package com.example.lab_3_media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.lab_3_media.fragments.MusicFragment
import com.example.lab_3_media.fragments.VideosFragment

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MusicFragment()
            }
            1 -> {
                VideosFragment()
            }
            else -> {
                MusicFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {
                return "Music"
            }
            1 -> {
                return "Videos"
            }
        }
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return 2
    }
}