package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.transition.Slide
import android.view.Gravity
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.R.layout
import com.linecy.interesting.data.Response
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.adapter.CustomAdapter
import kotlinx.android.synthetic.main.layout_recycler_view.recyclerView


/**
 *
 * Slide 动画。
 *
 * 将视图从边缘（上、下、左、右）移入和移出，可以设置插值器来改变速率,通过代码和创建transition动画来实现，
 * 是一个线性的偏移。
 *
 * @author by linecy.
 */
class SlideActivity : BaseActivity<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return R.layout.layout_recycler_view
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    window?.let {
      val slide = Slide()
      slide.slideEdge = Gravity.END
      slide.duration = 500
      it.enterTransition = slide
      it.exitTransition = slide
      it.reenterTransition = slide
      it.returnTransition = slide
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