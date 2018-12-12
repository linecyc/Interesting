package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.data.Response
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.adapter.CustomAdapter
import com.linecy.interesting.ui.adapter.CustomAdapter.OnItemClickListener
import com.linecy.interesting.utils.Constants
import kotlinx.android.synthetic.main.layout_recycler_view.recyclerView


/**
 *
 * Shared Element 动画。
 *
 * 两个页面元素的共享，实现一些特别的效果，同时可以配合其他属性，来更改元素的属性和动画。
 *
 * @author by linecy.
 */
class SharedElementActivity : BaseActivity<ViewDataBinding>(), OnItemClickListener {

  override fun layoutResId(): Int {
    return R.layout.layout_recycler_view
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    setBottomFabImageResource(R.drawable.ic_cached_white_24dp)
    val adapter = CustomAdapter(
      this,
      intArrayOf(R.layout.layout_shared_element_item, R.layout.layout_transition_header),
      intArrayOf(BR.sharedElementItem, BR.transitionHeader)
    )
    adapter.setOnItemClickListener(this)
    val layoutManager = GridLayoutManager(this, 2)
    layoutManager.spanSizeLookup = object : SpanSizeLookup() {
      override fun getSpanSize(p0: Int): Int {
        return if (p0 == 0) layoutManager.spanCount else 1
      }
    }
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = adapter
    adapter.refreshData(Response.createListData(10), Constants.PHOTO4_URL)
  }

  override fun onItemClick(view: View, data: Any?) {
    if (data is ListItem) {
      val ivLogo = view.findViewById<ImageView>(R.id.ivLogo)
      val tvName = view.findViewById<TextView>(R.id.tvName)
      val pair = Pair<View, String>(ivLogo, ivLogo.transitionName)
      val pair2 = Pair<View, String>(tvName, tvName.transitionName)
      Navigator.navigateToSharedElementDetail(this, data, pair, pair2)
    }
  }
}