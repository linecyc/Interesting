package com.linecy.interesting.ui.home

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.view.animation.Animation
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.databinding.FragmentTransitionViewBinding
import com.linecy.interesting.ui.BaseFragment
import com.linecy.interesting.ui.home.adapter.TransitionAdapter
import com.linecy.interesting.ui.misc.ViewContainer
import com.linecy.interesting.utils.AnimationUtils
import com.linecy.interesting.viewmodel.TransitionViewModel
import kotlinx.android.synthetic.main.fragment_transition_view.recyclerView
import kotlinx.android.synthetic.main.fragment_transition_view.swipeLayout
import kotlinx.android.synthetic.main.fragment_transition_view.viewContainer

/**
 * Transition相关。
 *
 * recyclerView 的id与RecyclerViewFragment 布局的recyclerView 的id一致时，会在创建的时候生成一样的id
 * 然后在bindData的时候导致两个页面数据都没有办法成功，虽然执行到了BindUIUtil类的对应方法，但是页面数据未成功。
 * 加载空数据和错误页能够成功加载，目前未找到根源问题，只是换了一个id解决此问题。
 *
 * 因为设置了相同的id，最终在R.id里面身生成的是一个id，在同一个布局中，在setContentView后，当前id是唯一的，
 * 所以在当前View树中可行；但是我在设置的时候，将作用域指向了activity?.run{}，在这个this里面我指向了TransitionViewFragment和
 * RecyclerViewFragment的父activity，导致他从activity开始遍历id，而RecyclerViewFragment是最先添加的，导致后面
 * 获取到的recyclerView是RecyclerViewFragment里面的，将当前类转换成kotlin byteCode可知这点。
 * FragmentActivity var2 = var10;
 * Intrinsics.checkExpressionValueIsNotNull(var2, "this");
 * TransitionAdapter transitionAdapter = new TransitionAdapter((Activity)var2);
 * RecyclerView var5 = (RecyclerView)var2.findViewById(id.recyclerView);

 * 所以我们才将当前页面的adapter设置到了第一个，同时导致当前页面的adapter没有设置，而在bindUIUtil方法里面是，
 * 对于第一个页面，我们的adapter不匹配设置的类型，所以跳过了数据填充，而第二个页面的adapter==null，导致也没有成功
 * 填充数据。所以注意作用域！！！！
 *
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
    //注意作用域
    activity?.let {
      val transitionAdapter =
        TransitionAdapter(it)
      recyclerView.run {
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