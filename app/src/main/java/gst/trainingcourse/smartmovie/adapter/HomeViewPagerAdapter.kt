package gst.trainingcourse.smartmovie.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gst.trainingcourse.smartmovie.ui.*

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MovieFragment.newInstance()
        1 -> PopularFragment.newInstance()
        2 -> TopRatedFragment.newInstance()
        3 -> UpComingFragment.newInstance()
        4 -> NowPlayingFragment.newInstance()
        else -> MovieFragment.newInstance()
    }

}