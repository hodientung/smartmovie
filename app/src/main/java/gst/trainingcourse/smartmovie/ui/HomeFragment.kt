package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.adapter.HomeViewPagerAdapter
import gst.trainingcourse.smartmovie.util.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewPagerAdapter = HomeViewPagerAdapter(this)
        vpHome.adapter = homeViewPagerAdapter
        vpHome.setPageTransformer(ZoomOutPageTransformer())
        TabLayoutMediator(
            tlHome, vpHome
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Movie"
                1 -> tab.text = "Popular"
                2 -> tab.text = "Top Rated"
                3 -> tab.text = "Up Coming"
                4 -> tab.text = "Now Playing"
            }
        }.attach()
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }
}