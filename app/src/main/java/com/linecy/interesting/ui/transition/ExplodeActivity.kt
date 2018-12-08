package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.transition.Explode
import com.linecy.interesting.R
import com.linecy.interesting.data.Response
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.adapter.TestAdapter
import kotlinx.android.synthetic.main.activity_transiton.recyclerView


/**
 *
 * Explode 动画。
 *
 * 同样是将视图从边缘移入和移出，不同于SLIDE的是他是“爆炸”式的效果，是由内到外的。
 * @author by linecy.
 */
class ExplodeActivity : BaseActivity<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return R.layout.activity_transiton
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    window?.let {
      val explode = Explode()
      //explode.mode = Explode.MODE_OUT
      //explode.mode = Explode.MODE_IN
      //设置扩散原点的位置
      //explode.epicenter
      explode.duration = 500
      it.enterTransition = explode
      it.exitTransition = explode
      it.reenterTransition = explode
      it.returnTransition = explode
    }
    setBottomFabImageResource(R.drawable.ic_arrow_upward_white_24dp)
    val adapter = TestAdapter(this)
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