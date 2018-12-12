package com.linecy.interesting.ui.home

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager.OnPageChangeListener
import com.linecy.interesting.R
import com.linecy.interesting.R.layout
import com.linecy.interesting.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.bottomNavigation
import kotlinx.android.synthetic.main.activity_main.viewPager

class MainActivity : BaseActivity<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return layout.activity_main
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    hideBottomAppBar()
    showSystemBottomNavigation()
    viewPager.run {
      adapter = FragmentAdapter(
        listOf(RecyclerViewFragment(), TransitionViewFragment(), RotateFragment()),
        supportFragmentManager
      )
      offscreenPageLimit = 2
      currentItem = 0
      addOnPageChangeListener(object : OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {}

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

        override fun onPageSelected(p0: Int) {
          bottomNavigation.menu.getItem(p0).isChecked = true
        }
      })
    }
    bottomNavigation.setOnNavigationItemSelectedListener {
      return@setOnNavigationItemSelectedListener when (it.itemId) {
        R.id.navigation_recycler_view -> {
          viewPager.currentItem = 0
          true
        }
        R.id.navigation_transition -> {
          viewPager.currentItem = 1
          true
        }
        R.id.navigation_profile -> {
          viewPager.currentItem = 2
          true
        }
        else -> false
      }
    }
  }

  inner class FragmentAdapter(private val list: List<Fragment>, fm: FragmentManager?) :
    FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
      return list[p0]
    }

    override fun getCount(): Int {
      return list.size
    }
  }
}
