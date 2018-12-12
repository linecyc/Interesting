package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.transition.Fade
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.R.layout
import com.linecy.interesting.data.Response
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.adapter.CustomAdapter
import kotlinx.android.synthetic.main.layout_recycler_view.recyclerView


/**
 *
 * Fade 动画。
 *
 * 这是一个将视图层级渐变的效果，淡入淡出。
 *
 * @author by linecy.
 */
class FadeActivity : BaseActivity<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return R.layout.layout_recycler_view
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    window?.let {
      val fade = Fade()
      fade.duration = 500
      it.enterTransition = fade
      it.exitTransition = fade
      it.reenterTransition = fade
      it.returnTransition = fade
    }
    setBottomFabImageResource(R.drawable.ic_arrow_upward_white_24dp)
    val adapter = CustomAdapter(this, intArrayOf(layout.layout_item), intArrayOf(BR.item))
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter
    adapter.refreshData(Response.createListData(10))
  }

  override fun onBottomAppBarFab() {
    if ((recyclerView.adapter?.itemCount ?: 0) > 0) {
      recyclerView.scrollToPosition(0)
    }
  }
}