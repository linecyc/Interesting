package com.linecy.interesting.ui.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.animation.Animation
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.R.color
import com.linecy.interesting.databinding.FragmentRecyclerViewBinding
import com.linecy.interesting.ui.BaseFragment
import com.linecy.interesting.ui.home.adapter.RecyclerViewAdapter
import com.linecy.interesting.ui.misc.ItemTouchCallback
import com.linecy.interesting.ui.misc.StickyHeaderDecoration
import com.linecy.interesting.ui.misc.ViewContainer
import com.linecy.interesting.utils.AnimationUtils
import com.linecy.interesting.utils.DisplayUtils
import com.linecy.interesting.viewmodel.RecyclerViewViewModel
import kotlinx.android.synthetic.main.fragment_recycler_view.recyclerView
import kotlinx.android.synthetic.main.fragment_recycler_view.swipeLayout
import kotlinx.android.synthetic.main.fragment_recycler_view.viewContainer

/**
 * RecyclerView 相关。
 *
 * @author by linecy.
 */

class RecyclerViewFragment : BaseFragment<FragmentRecyclerViewBinding>(), OnRefreshListener,
  ViewContainer.EmptyCallback, ViewContainer.ErrorCallback {

  private lateinit var viewModel: RecyclerViewViewModel

  override fun layoutResId(): Int {
    return R.layout.fragment_recycler_view
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    swipeLayout.setOnRefreshListener(this)
    swipeLayout.setColorSchemeResources(color.colorPrimary)
    viewContainer.setEmptyCallback(this)
    viewContainer.setErrorCallback(this)
    viewModel = viewModelProvider.get(RecyclerViewViewModel::class.java)
    fragmentBinding?.setVariable(BR.recyclerViewViewModel, viewModel)
    activity?.let {
      val recyclerViewAdapter = RecyclerViewAdapter(it)
      recyclerView.run {
        adapter = recyclerViewAdapter
        val manager = LinearLayoutManager(context)
        layoutManager = manager
        val decoration = StickyHeaderDecoration.builder()
          .headerView(layoutInflater.inflate(R.layout.layout_stick_header, null))
          .drawDecoration(true)
          .decorationColor(ContextCompat.getColor(context, R.color.colorPrimary))
          .decorationMarginLeft(DisplayUtils.dp2px(context, 16f).toInt())
          .decorationMarginRight(DisplayUtils.dp2px(context, 16f).toInt())
          .build()
        addItemDecoration(decoration)

        //ItemTouchHelper会与viewPager冲突
        val left = layoutInflater.inflate(R.layout.layout_left, null)
        val right = layoutInflater.inflate(R.layout.layout_right, null)
        ItemTouchHelper(object :
          ItemTouchCallback(
            leftOptionView = left,
            rightOptionView = right,
            swipeRefreshLayout = swipeLayout
          ) {
          override fun onItemRemoved(position: Int) {
            recyclerViewAdapter.onItemRemoved(position)
          }

        }).attachToRecyclerView(this)
      }
      onRefresh()
    }
  }


  override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
    return when (nextAnim) {
      R.anim.rotate_3d_exit -> AnimationUtils.createExitRotate3dAnimation()
      //因为进入动画为了和退出动画配合，设了一个延时，所以第一次进入的时候也有个延时，只是因为很短，目前忽略
      R.anim.rotate_3d_enter -> AnimationUtils.createEnterRotate3dAnimation()
      else -> super.onCreateAnimation(transit, enter, nextAnim)
    }
  }

  override fun onRefresh() {
    viewModel.refreshData()
  }

  override fun onShowEmpty() {
    onRefresh()
  }

  override fun onShowError() {
    onRefresh()
  }

}