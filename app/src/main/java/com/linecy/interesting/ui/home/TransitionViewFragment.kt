package com.linecy.interesting.ui.home

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.databinding.FragmentTransitionViewBinding
import com.linecy.interesting.ui.BaseFragment
import com.linecy.interesting.ui.home.adapter.TransitionAdapter
import com.linecy.interesting.ui.misc.ViewContainer
import com.linecy.interesting.viewmodel.TransitionViewModel
import kotlinx.android.synthetic.main.fragment_transition_view.recyclerViewTrans
import kotlinx.android.synthetic.main.fragment_transition_view.swipeLayout
import kotlinx.android.synthetic.main.fragment_transition_view.viewContainer

/**
 * Transition相关。
 *
 * recyclerView 的id与RecyclerViewFragment 布局的recyclerView 的id一致时，会在创建的时候生成一样的id
 * 然后在bindData的时候导致两个页面数据都没有办法成功，虽然执行到了BindUIUtil类的对应方法，但是页面数据未成功加载
 * 空数据和错误页能够成功加载，目前未找到根源问题，只是换了一个id解决此问题。
 *
 * @author by linecy.
 */
class TransitionViewFragment : BaseFragment<FragmentTransitionViewBinding>(),
  SwipeRefreshLayout.OnRefreshListener, ViewContainer.EmptyCallback, ViewContainer.ErrorCallback {

  private lateinit var viewModel: TransitionViewModel

  override fun layoutResId(): Int {
    return R.layout.fragment_transition_view
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    swipeLayout.setOnRefreshListener(this)
    swipeLayout.setColorSchemeResources(R.color.colorPrimary)
    viewContainer.setEmptyCallback(this)
    viewContainer.setErrorCallback(this)
    viewModel = viewModelProvider.get(TransitionViewModel::class.java)
    fragmentBinding?.setVariable(BR.transitionViewModel, viewModel)
    activity?.run {
      val transitionAdapter =
        TransitionAdapter(this)
      recyclerViewTrans.run {
        adapter = transitionAdapter
        layoutManager = LinearLayoutManager(context)
      }
      onRefresh()
    }
  }


  /**
   * 因为这个页面点击跳转有很多过渡动画，如果不重置的话，可能会影响其他fragment跳转的动画效果
   * 保持和style一致
   */
  override fun onResume() {
    super.onResume()
    activity?.window?.let {
      val slideTop = TransitionInflater.from(activity).inflateTransition(R.transition.slide_top)
      val slideBottom =
        TransitionInflater.from(activity).inflateTransition(R.transition.slide_bottom)
      it.enterTransition = slideTop
      it.exitTransition = slideBottom
      it.reenterTransition = slideTop
      it.returnTransition = slideBottom
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