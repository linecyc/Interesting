package com.linecy.interesting.ui.transition

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import com.linecy.interesting.R
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.utils.BindUIUtil
import kotlinx.android.synthetic.main.activity_circular_reveal.content
import kotlinx.android.synthetic.main.activity_circular_reveal.imageView
import kotlinx.android.synthetic.main.activity_circular_reveal.ivBlue
import kotlinx.android.synthetic.main.activity_circular_reveal.ivGreen
import kotlinx.android.synthetic.main.activity_circular_reveal.ivRed
import kotlinx.android.synthetic.main.activity_circular_reveal.ivYellow

/**
 * 圆形剪辑圈效果,展示揭露或隐藏动画。
 *
 * @author by linecy.
 */
class CircularRevealActivity : BaseActivity<ViewDataBinding>(), OnClickListener {

  private lateinit var interpolator: Interpolator

  override fun layoutResId(): Int {
    return R.layout.activity_circular_reveal
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    BindUIUtil.loadCircleUrl(imageView, intent.getStringExtra(Navigator.EXTRA_URL))
    imageView.transitionName = getString(R.string.transitionNameImage)
    interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)

    ivRed.setOnClickListener(this)
    imageView.setOnClickListener(this)
    ivYellow.setOnClickListener(this)
    ivBlue.setOnClickListener(this)
    ivGreen.setOnClickListener(this)
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.ivRed -> animReveal(
        content,
        R.color.red,
        0,
        0, false
      )

      R.id.ivYellow -> animReveal(
        content,
        R.color.yellow,
        content.width,
        0, false
      )

      R.id.imageView -> animReveal(
        content,
        R.color.colorPrimary,
        content.width / 2,
        content.height / 2, true
      )

      R.id.ivBlue -> animReveal(
        content,
        R.color.blue,
        0,
        content.height, false
      )
      R.id.ivGreen -> animReveal(
        content,
        R.color.green,
        content.width,
        content.height, false
      )
    }
  }

  private fun animReveal(
    viewRoot: ViewGroup, @ColorRes color: Int,
    x: Int,
    y: Int,
    maxToMin: Boolean
  ) {
    val finalRadius = Math.hypot(viewRoot.width.toDouble(), viewRoot.height.toDouble()).toFloat()

    val anim = if (maxToMin) {
      ViewAnimationUtils.createCircularReveal(viewRoot, x, y, finalRadius, 0f)
    } else {
      ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0f, finalRadius)
    }
    viewRoot.setBackgroundColor(ContextCompat.getColor(this, color))
    anim.duration = resources.getInteger(R.integer.anim_duration_long).toLong()
    anim.interpolator = AccelerateDecelerateInterpolator()
    anim.addListener(object : AnimatorListener {
      override fun onAnimationRepeat(animation: Animator?) {}

      override fun onAnimationEnd(animation: Animator?) {
        animIn()
        if (maxToMin) {
          viewRoot.background = null
        }
      }

      override fun onAnimationCancel(animation: Animator?) {
        animIn()
      }

      override fun onAnimationStart(animation: Animator?) {
        animOut()
      }
    })
    anim.start()
  }

  private fun animIn() {
    for (i in 0 until content.childCount) {
      val child = content.getChildAt(i)
      child.clipToOutline = false
      child.animate()
        .setStartDelay(100L + i * 100)
        .setInterpolator(interpolator)
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
    }
  }

  private fun animOut() {
    for (i in 0 until content.childCount) {
      val child = content.getChildAt(i)
      child.clipToOutline = false
      child.animate()
        .setStartDelay(i.toLong())
        .setInterpolator(interpolator)
        .alpha(0f)
        .scaleX(0f)
        .scaleY(0f)
    }
  }
}