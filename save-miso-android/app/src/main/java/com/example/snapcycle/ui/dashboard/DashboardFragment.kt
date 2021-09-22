package com.example.snapcycle.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.snapcycle.R
import com.example.snapcycle.TabFragment1
import com.example.snapcycle.TabFragment2
import com.example.snapcycle.TabFragment3
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

// PagerAdapter implements the FragmentStateAdapter interface
// Responsible for creating individual tab fragments when tab layout item is selected

class DashboardFragment : Fragment() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var pagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        tabLayout = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.pager)
        pagerAdapter = PagerAdapter(this, tabLayout.tabCount)

        viewPager.adapter = pagerAdapter
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
//                 TabLayoutMediator.attach() removes all tabs before adding them back again
                0 -> {
                    tab.text = getString(R.string.instructionsMenu1)
                }
                1 -> {
                    tab.text = getString(R.string.instructionsMenu2)
                }
                2 -> {
                    tab.text = getString(R.string.instructionsMenu3)
                }
            }
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
        return view
    }

    class PagerAdapter(f: Fragment?, private var count: Int) :
        FragmentStateAdapter(f!!) {
        lateinit var currFragment: Fragment

        override fun getItemCount(): Int {
            return count
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    currFragment = TabFragment1()
                }
                1 -> {
                    currFragment = TabFragment2()
                }
                2 -> {
                    currFragment = TabFragment3()
                }
                else -> {
                    currFragment = TabFragment1()
                }
            }
            return currFragment
        }

    }

}