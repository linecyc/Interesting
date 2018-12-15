package com.linecy.interesting.ui.transition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import com.linecy.interesting.R
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.transition.rotate.RotateFragmentV11
import com.linecy.interesting.ui.transition.rotate.RotateFragmentV4
import com.linecy.interesting.ui.weight.Rotate3dAnimator
import com.linecy.interesting.utils.AnimationUtils
import com.linecy.interesting.utils.AnimatorUtils
import kotlinx.android.synthetic.main.activity_rotate.flLeftBottom
import kotlinx.android.synthetic.main.activity_rotate.flLeftTop
import kotlinx.android.synthetic.main.activity_rotate.flRightBottom
import kotlinx.android.synthetic.main.activity_rotate.flRightTop
import kotlinx.android.synthetic.main.activity_rotate.tvAnimation
import kotlinx.android.synthetic.main.activity_rotate.tvAnimation2
import kotlinx.android.synthetic.main.activity_rotate.tvAnimator
import kotlinx.android.synthetic.main.activity_rotate.tvAnimator2

/**
 * @author by linecy.
 */
class RotateActivity : BaseActivity<ViewDataBinding>(), OnClickListener {

  private var exitAnimation: Animation? = null
  private var enterAnimation: Animation? = null
  private var exitAnimator: Rotate3dAnimator? = null
  private var enterAnimator: Rotate3dAnimator? = null

  override fun layoutResId(): Int {
    return R.layout.activity_rotate
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    flLeftTop.setOnClickListener(this)
    flRightTop.setOnClickListener(this)
    flLeftBottom.setOnClickListener(this)
    flRightBottom.setOnClickListener(this)
    flLeftTop.tag = false
    RotateFragmentV4.rotate(
      supportFragmentManager,
      R.id.flLeftTop,
      true
    )

    flRightTop.tag = false
    RotateFragmentV11.rotate(
      fragmentManager,
      R.id.flRightTop,
      true
    )
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.flLeftTop -> {
        RotateFragmentV4.rotate(
          supportFragmentManager,
          R.id.flLeftTop,
          flLeftTop.tag == true
        )
        flLeftTop.tag = !(flLeftTop.tag as Boolean)
      }
      R.id.flRightTop -> {
        RotateFragmentV11.rotate(
          fragmentManager,
          R.id.flRightTop,
          flRightTop.tag == true
        )
        flRightTop.tag = !(flRightTop.tag as Boolean)
      }
      R.id.flLeftBottom -> {
        startAnimation()
      }
      R.id.flRightBottom -> {
        startAnimator()
      }
    }
  }

  private fun startAnimation() {
    exitAnimation = AnimationUtils.createExitRotate3dAnimation()
    enterAnimation = AnimationUtils.createEnterRotate3dAnimation()
    if (true != tvAnimation.tag) {
      tvAnimation.visibility = View.GONE
      tvAnimation2.visibility = View.VISIBLE
      tvAnimation.startAnimation(exitAnimation)
      tvAnimation2.startAnimation(enterAnimation)
      tvAnimation.tag = true
    } else {
      tvAnimation.visibility = View.VISIBLE
      tvAnimation2.visibility = View.GONE
      tvAnimation2.startAnimation(exitAnimation)
      tvAnimation.startAnimation(enterAnimation)
      tvAnimation.tag = false
    }
  }

  private fun startAnimator() {
    if (exitAnimator == null || enterAnimator == null) {
      exitAnimator = AnimatorUtils.createExitAnimator()
      enterAnimator = AnimatorUtils.createEnterAnimator()
    }
    if (true != tvAnimator.tag) {
      exitAnimator?.run {
        setTarget(tvAnimator2)
        addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationStart(animation: Animator?) {
            this@run.getTarget()?.visibility = View.VISIBLE
            tvAnimator.visibility = View.GONE
            super.onAnimationStart(animation)
          }

          override fun onAnimationEnd(animation: Animator) {
            this@run.getTarget()?.visibility = View.GONE
            tvAnimator.visibility = View.VISIBLE
            super.onAnimationEnd(animation)
          }
        })
        start()
      }
      enterAnimator?.run {
        setTarget(tvAnimator)
        start()
      }
      tvAnimator.tag = true
    } else {
      exitAnimator?.run {
        setTarget(tvAnimator)
        addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationStart(animation: Animator?) {
            tvAnimator.visibility = View.VISIBLE
            tvAnimator2.visibility = View.GONE
            super.onAnimationStart(animation)
          }

          override fun onAnimationEnd(animation: Animator) {
            tvAnimator.visibility = View.GONE
            tvAnimator2.visibility = View.VISIBLE
            super.onAnimationEnd(animation)
          }
        })
        start()
      }
      enterAnimator?.run {
        setTarget(tvAnimator2)
        start()
      }
      tvAnimator.tag = false
    }
  }
}