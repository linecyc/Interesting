package com.linecy.interesting.ui.home

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.linecy.interesting.R
import com.linecy.interesting.R.layout
import com.linecy.interesting.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.bottomNavigation

class MainActivity : BaseActivity<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return layout.activity_main
  }

  override fun onInitView(savedInstanceState: Bundle?) {

    hideBottomAppBar()
    showSystemBottomNavigation()
    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out)

    if (savedInstanceState == null) {
      setCurrentItem(0)
    }
    bottomNavigation.setOnNavigationItemSelectedListener {
      return@setOnNavigationItemSelectedListener when (it.itemId) {
        R.id.navigation_recycler_view -> {
          setCurrentItem(0)
          true
        }
        R.id.navigation_transition -> {
          setCurrentItem(1)
          true
        }
        R.id.navigation_profile -> {
          setCurrentItem(2)
          true
        }
        else -> false
      }
    }
  }

  private fun setCurrentItem(position: Int) {
    val ft = supportFragmentManager.beginTransaction()
    ft.setCustomAnimations(R.anim.rotate_3d_enter, R.anim.rotate_3d_exit)
    val existence =
      when (position) {
        0 -> supportFragmentManager.findFragmentByTag(RecyclerViewFragment::class.java.simpleName)
        1 -> supportFragmentManager.findFragmentByTag(TransitionViewFragment::class.java.simpleName)
        else -> supportFragmentManager.findFragmentByTag(RotateFragment::class.java.simpleName)
      }
    existence?.run {
      ft.show(this)
    } ?: create(position, ft)

    supportFragmentManager.fragments.filter {
      it != existence
    }.forEach {
      ft.hide(it)
    }
    ft.commit()
    bottomNavigation.menu.getItem(position).isChecked = true
  }

  private fun create(position: Int, ft: FragmentTransaction) {
    val create = when (position) {
      0 -> RecyclerViewFragment()
      1 -> TransitionViewFragment()
      else -> RotateFragment()
    }
    ft.add(
      R.id.fragmentLayout,
      create,
      create::class.java.simpleName
    )
  }
}
