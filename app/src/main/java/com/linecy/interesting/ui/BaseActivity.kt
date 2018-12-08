package com.linecy.interesting.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.linecy.interesting.R
import kotlinx.android.synthetic.main.layout_base.baseView
import kotlinx.android.synthetic.main.layout_base.bottomAppBar
import kotlinx.android.synthetic.main.layout_base.bottomFab


/**
 * @author by linecy.
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

  private var activityBinding: VB? = null

  private var showBottomAppBar: Boolean = true
  private var hideSystemBottomNav: Boolean = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val layoutId = layoutResId()
    if (0 == layoutId) {
      throw IllegalArgumentException("You must me set layout resource id now.")
    }
    setContentView(layoutId)
    setSupportActionBar(bottomAppBar)
    bottomAppBar.setNavigationOnClickListener {
      onBottomAppBarUp()
    }
    bottomFab.setOnClickListener {
      onBottomAppBarFab()
    }
    onInitView(savedInstanceState)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    return if (showBottomAppBar) {
      menuInflater.inflate(R.menu.navigation_bottom_app_bar, menu)
      true
    } else {
      super.onCreateOptionsMenu(menu)
    }
  }

  override fun setContentView(layoutResID: Int) {
    val rootView = findViewById<ViewGroup>(android.R.id.content)
    rootView.removeAllViews()
    layoutInflater.inflate(R.layout.layout_base, rootView)
    activityBinding = DataBindingUtil.inflate(layoutInflater, layoutResID, baseView, false)

    //将传入的布局设为第一个，保持bottomAppBar漂浮在布局之上
    //减少一层fragmentLayout之类的，避免再次add
    if (null == activityBinding) {
      val view = layoutInflater.inflate(layoutResID, baseView, false)
      baseView.addView(view, 0)
    } else {
      baseView.addView(activityBinding?.root, 0)
    }
  }

  override fun onResume() {
    super.onResume()
    //解决解除锁屏后重新展示底部虚拟按键问题，
    //会与bottomAppBar冲突？？？已解决，详见ScaleDownShowBehavior
    if (hideSystemBottomNav) {
      hideSystemBottomNavigation()
    }
  }

  abstract fun layoutResId(): Int

  abstract fun onInitView(savedInstanceState: Bundle?)

  protected fun showSystemBottomNavigation() {
    this.hideSystemBottomNav = false
  }

  protected fun hideSystemBottomNavigation() {
    window?.decorView?.let {
      it.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
  }

  /**
   * 返回点击回调
   */
  protected open fun onBottomAppBarUp() {
    supportFinishAfterTransition()
  }


  /**
   * bottomFab 点击回调
   */
  protected open fun onBottomAppBarFab() {

  }

  /**
   * 隐藏底部导航栏
   */
  protected fun hideBottomAppBar() {
    bottomAppBar.visibility = View.GONE
    bottomFab.hide()
  }

  /**
   * 设置bottomFab icon
   */
  protected fun setBottomFabImageResource(@DrawableRes resId: Int) {
    bottomFab.setImageResource(resId)
  }

  protected fun setBottomFabImageDrawable(drawable: Drawable?) {
    bottomFab.setImageDrawable(drawable)
  }

  override fun onBackPressed() {
    supportFinishAfterTransition()
  }
}