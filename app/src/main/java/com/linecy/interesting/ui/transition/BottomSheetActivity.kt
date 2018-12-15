package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.R.layout
import com.linecy.interesting.data.Response
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.adapter.CustomAdapter
import com.linecy.interesting.ui.home.bottomsheet.BottomDrawerFragment
import kotlinx.android.synthetic.main.layout_recycler_view.recyclerView


/**
 * @author by linecy.
 */
class BottomSheetActivity : BaseActivity<ViewDataBinding>() {

  private var bottomSheetDialog: BottomSheetDialog? = null

  override fun layoutResId(): Int {
    return R.layout.activity_bottom_sheet
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    setBottomFabImageResource(R.drawable.ic_arrow_upward_white_24dp)
    val adapter = CustomAdapter(this, intArrayOf(layout.layout_item), intArrayOf(BR.item))
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter
    adapter.refreshData(Response.createListData(10))
    //BindUIUtil.loadUrl(imageView, intent.getStringExtra(Navigator.EXTRA_URL))
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
          BottomSheetActivity::class.java.simpleName
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
        LayoutInflater.from(this).inflate(R.layout.layout_recycler_view, null, false)
      val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
      val adapter = CustomAdapter(this, intArrayOf(R.layout.layout_item), intArrayOf(BR.item))
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