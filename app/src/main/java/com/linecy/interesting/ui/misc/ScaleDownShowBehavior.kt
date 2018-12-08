package com.linecy.interesting.ui.misc

import android.animation.Animator
import android.content.Context
import android.support.design.bottomappbar.BottomAppBar
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.linecy.interesting.R
import com.linecy.interesting.utils.AnimatorUtils

/**
 * @author by linecy.
 */

private const val DEFAULT_SIZE = 20000f

@Suppress("unused")
class ScaleDownShowBehavior(context: Context?, attrs: AttributeSet?) :
  FloatingActionButton.Behavior(context, attrs) {
  // 隐藏动画是否正在执行
  private var isAnimatingEnd = true
  private var flbStartY: Float = 20010f
  private var bottomStartY: Float = 20010f
  private var flbEndY: Float = 0f
  private var bottomEndY: Float = 0f
  private val listener = BottomAppBarAnimatorListener()

  override fun onStartNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: FloatingActionButton,
    directTargetChild: View,
    target: View,
    axes: Int,
    type: Int
  ): Boolean {
    return axes == ViewCompat.SCROLL_AXIS_VERTICAL
  }

  override fun onNestedScroll(
    coordinatorLayout: CoordinatorLayout,
    child: FloatingActionButton,
    target: View,
    dxConsumed: Int,
    dyConsumed: Int,
    dxUnconsumed: Int,
    dyUnconsumed: Int,
    type: Int
  ) {
    //自身滚动Consumed
    //用户主动Unconsumed
    if ((dyConsumed < 0 || dyUnconsumed < 0) && isAnimatingEnd && !child.isEnabled) {// 显示
      val bottomAppBar = coordinatorLayout.findViewById<BottomAppBar>(R.id.bottomAppBar)
      child.isEnabled = true
      bottomAppBar.visibility = View.VISIBLE
      AnimatorUtils.transitionAndAlpha(
        child,
        bottomAppBar,
        0f,
        1f,
        flbEndY,
        flbStartY,
        flbEndY,
        bottomStartY,
        false,
        listener
      )
    } else if ((dyConsumed > 0 || dyUnconsumed > 0) && isAnimatingEnd
      && child.isEnabled
    ) {
      //隐藏
      val bottomAppBar = coordinatorLayout.findViewById<BottomAppBar>(R.id.bottomAppBar)
      //记录初始偏移
      if (flbStartY > DEFAULT_SIZE) {
        flbStartY = child.top + child.translationY
        flbEndY = bottomAppBar.bottom.toFloat()
        bottomStartY = bottomAppBar.top.toFloat()
        bottomEndY = flbEndY + flbEndY - bottomStartY
      }
      AnimatorUtils.transitionAndAlpha(
        child,
        bottomAppBar,
        1f,
        0f,
        flbStartY,
        flbEndY,
        bottomStartY,
        flbEndY,
        true,
        listener
      )
    }
  }


  inner class BottomAppBarAnimatorListener : Animator.AnimatorListener {
    private var isHide: Boolean = true
    private var fabView: View? = null
    private var bottomView: View? = null

    fun setIsHide(
      isHide: Boolean,
      fabView: View? = null,
      bottomView: View? = null
    ): Animator.AnimatorListener {
      this.isHide = isHide
      this.fabView = fabView
      this.bottomView = bottomView
      return this
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
      if (isHide) {
        //如果fabView设置成Invisible或者gone的话，会导致bottomView在重新展示后没有预留margin
        //api>=25的时候，gone会直接跳过，导致隐藏后不再显示
        //invisible的时候，在隐藏系统底部虚拟按键后，resume页面后，如果bottomView和flbView此时滑动到页面外，
        //那么会导致重新划出来的时候导致bottomView完全填充，没有预留flbView的位置和边距
        //flbView监听了动画了状态，同时配合自身是否是visible来影响bottomView
        //所以这里使用了isEnabled来判断，当然也可以用bottomView的Visible，只是要每次去获取罢了
        fabView?.isEnabled = false
        bottomView?.visibility = View.GONE
      }
      isAnimatingEnd = true
    }

    override fun onAnimationCancel(animation: Animator?) {
      isAnimatingEnd = true
    }

    override fun onAnimationStart(animation: Animator?) {
      isAnimatingEnd = false
    }
  }
}