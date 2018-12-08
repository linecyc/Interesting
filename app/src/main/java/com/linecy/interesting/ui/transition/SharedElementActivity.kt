package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.data.Response
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.adapter.CustomAdapter
import com.linecy.interesting.ui.adapter.TestAdapter
import com.linecy.interesting.ui.home.BottomDrawerFragment
import com.linecy.interesting.utils.Constants
import kotlinx.android.synthetic.main.activity_transiton.recyclerView


/**
 *
 * Shared Element 动画。
 *
 * 两个页面元素的共享，实现一些特别的效果，同时可以配合其他属性，来更改元素的属性和动画。
 *
 * @author by linecy.
 */
class SharedElementActivity : BaseActivity<ViewDataBinding>() {

  private var bottomSheetDialog: BottomSheetDialog? = null

  override fun layoutResId(): Int {
    return R.layout.activity_transiton
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    setBottomFabImageResource(R.drawable.ic_cached_white_24dp)
    val adapter = CustomAdapter(
      this,
      intArrayOf(R.layout.layout_shared_element_item, R.layout.layout_transition_header),
      intArrayOf(BR.sharedElementItem, BR.transitionHeader)
    )
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

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.navigation_search -> {
        showBottomSheetDialog()
        true
      }
      R.id.navigation_menu
      -> {
        BottomDrawerFragment.getInstance(R.menu.bottom_normal_menu).show(
          supportFragmentManager,
          SharedElementActivity::class.java.simpleName
        )
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun showBottomSheetDialog() {
    if (bottomSheetDialog == null) {
      bottomSheetDialog = BottomSheetDialog(this)
      val view =
        LayoutInflater.from(this).inflate(R.layout.layout_test, null, false)
      val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTest)
      val adapter = TestAdapter(this)
      recyclerView.layoutManager = LinearLayoutManager(this)
      recyclerView.adapter = adapter
      bottomSheetDialog?.setContentView(view)
      bottomSheetDialog?.setOnDismissListener {
        hideSystemBottomNavigation()
      }
      adapter.refreshData(Response.createListData(10))
    }
    bottomSheetDialog?.show()
  }

  private fun hideBottomSheetDialog() {
    if (bottomSheetDialog?.isShowing == true) {
      bottomSheetDialog?.dismiss()
    }
  }

  override fun onDestroy() {
    hideBottomSheetDialog()
    super.onDestroy()
  }

  override fun onBottomAppBarFab() {
    if ((recyclerView.adapter?.itemCount ?: 0) > 0) {
      recyclerView.scrollToPosition(0)
    }
  }
}