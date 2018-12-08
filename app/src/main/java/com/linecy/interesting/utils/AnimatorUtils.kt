package com.linecy.interesting.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import com.linecy.interesting.ui.misc.ScaleDownShowBehavior.BottomAppBarAnimatorListener

/**
 * 动画工具类。
 *
 * @author by linecy.
 */

//如果为-180f,可以改变转动方向
private const val DEFAULT_SIZE = 180f

object AnimatorUtils {

  /**
   * 竖直翻转
   */
  fun rotationX(view: View, listener: ViewPropertyAnimatorListener) {
    ViewCompat.animate(view)
      .rotationX(view.rotationX + DEFAULT_SIZE)
      .setDuration(500)
      .setListener(listener)
      .start()
  }


  fun rotationX(front: View, back: View) {
    val set = AnimatorSet()
    val anim1: Animator
    val anim2: Animator
    //当前是展示的正面，还是反面
    val isFront = front.visibility == View.VISIBLE && back.visibility == View.GONE
    if (isFront) {
      anim1 = ObjectAnimator
        .ofFloat(front, "rotationX", 0f, 90f)
        .setDuration(250)
      anim2 = ObjectAnimator
        .ofFloat(back, "rotationX", -90f, 0f)
        .setDuration(250)
    } else {
      anim1 = ObjectAnimator
        .ofFloat(back, "rotationX", 0f, 90f)
        .setDuration(250)
      anim2 = ObjectAnimator
        .ofFloat(front, "rotationX", -90f, 0f)
        .setDuration(250)
    }

    anim1.addListener(object : Animator.AnimatorListener {
      override fun onAnimationStart(animation: Animator) {
      }

      override fun onAnimationEnd(animation: Animator) {
        if (isFront) {
          front.visibility = View.GONE
          back.visibility = View.VISIBLE
        } else {
          front.visibility = View.VISIBLE
          back.visibility = View.GONE
        }
      }

      override fun onAnimationCancel(animation: Animator) {
      }

      override fun onAnimationRepeat(animation: Animator) {
      }
    })
    anim2.addListener(object : Animator.AnimatorListener {
      override fun onAnimationStart(animation: Animator) {}

      override fun onAnimationEnd(animation: Animator) {
        if (isFront) {
          front.rotationX = 0f
        } else {
          back.rotationX = 0f
        }
      }

      override fun onAnimationCancel(animation: Animator) {
      }

      override fun onAnimationRepeat(animation: Animator) {
      }
    })
    set.play(anim1).before(anim2)
    set.start()
  }

  /**
   * 水平翻转
   */
  fun rotationY(view: View, listener: ViewPropertyAnimatorListener) {
    ViewCompat.animate(view)
      .rotationY(view.rotationY + DEFAULT_SIZE)
      .setDuration(500)
      .setListener(listener)
      .start()
    setCameraDistance(view)
  }

  fun rotationY(front: View, back: View) {
    val set = AnimatorSet()
    val anim1: Animator
    val anim2: Animator
    //当前是展示的正面，还是反面
    val isFront = front.visibility == View.VISIBLE && back.visibility == View.GONE
    if (isFront) {
      anim1 = ObjectAnimator
        .ofFloat(front, "rotationY", 0f, 90f)
        .setDuration(250)
      anim2 = ObjectAnimator
        .ofFloat(back, "rotationY", -90f, 0f)
        .setDuration(250)
    } else {
      anim1 = ObjectAnimator
        .ofFloat(back, "rotationY", 0f, 90f)
        .setDuration(250)
      anim2 = ObjectAnimator
        .ofFloat(front, "rotationY", -90f, 0f)
        .setDuration(250)
    }

    anim1.addListener(object : Animator.AnimatorListener {
      override fun onAnimationStart(animation: Animator) {
      }

      override fun onAnimationEnd(animation: Animator) {
        if (isFront) {
          front.visibility = View.GONE
          back.visibility = View.VISIBLE
        } else {
          front.visibility = View.VISIBLE
          back.visibility = View.GONE
        }
      }

      override fun onAnimationCancel(animation: Animator) {
      }

      override fun onAnimationRepeat(animation: Animator) {
      }
    })
    anim2.addListener(object : Animator.AnimatorListener {
      override fun onAnimationStart(animation: Animator) {
      }

      override fun onAnimationEnd(animation: Animator) {
        if (isFront) {
          front.rotationY = 0f
        } else {
          back.rotationY = 0f
        }
      }

      override fun onAnimationCancel(animation: Animator) {
      }

      override fun onAnimationRepeat(animation: Animator) {
      }
    })
    set.play(anim1).before(anim2)
    set.start()
    setCameraDistance(if (isFront) front else back)
  }

  /**
   * 底部导航栏偏移和透明动画
   */
  fun transitionAndAlpha(
    fabView: View,
    bottomView: View,
    alphaFrom: Float,
    alphaTo: Float,
    flbFrom: Float,
    flbTo: Float,
    bottomFrom: Float,
    bottomTo: Float,
    isHide: Boolean,
    listener: BottomAppBarAnimatorListener
  ) {
    val set = AnimatorSet()
    val anim1 = ObjectAnimator.ofFloat(fabView, "alpha", alphaFrom, alphaTo)
    val anim2 = ObjectAnimator.ofFloat(bottomView, "alpha", alphaFrom, alphaTo)
    val anim3 =
      ObjectAnimator.ofFloat(fabView, "y", flbFrom, flbTo)
    val anim4 =
      ObjectAnimator.ofFloat(bottomView, "y", bottomFrom, bottomTo)
    set.setDuration(500).interpolator = LinearOutSlowInInterpolator()
    set.addListener(listener.setIsHide(isHide, fabView, bottomView))
    set.playTogether(anim1, anim2, anim3, anim4)
    set.start()
  }


  /**
   * 设置视角，不然翻转会超出屏幕
   */
  private fun setCameraDistance(view: View) {
    val distance = 16000
    val scale = view.resources.displayMetrics.density * distance
    view.cameraDistance = scale
  }
}