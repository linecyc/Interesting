package com.linecy.interesting.ui.transition.rotate

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.linecy.interesting.R
import com.linecy.interesting.utils.AnimatorUtils

/**
 * @author by linecy.
 */

class RotateFragmentV11 @SuppressLint("ValidFragment")
constructor() : Fragment() {

  companion object {

    private const val TAG_FRAGMENT1 = "show1"
    private const val TAG_FRAGMENT2 = "show2"
    private const val EXTRA_DATA = "extra_data"

    fun rotate(
      fragmentManager: FragmentManager, @IdRes containerId: Int, isFront: Boolean
    ) {
      val show = if (isFront) TAG_FRAGMENT1 else TAG_FRAGMENT2
      val hide = if (isFront) TAG_FRAGMENT2 else TAG_FRAGMENT1
      val ft = fragmentManager.beginTransaction()
      ft.setCustomAnimations(R.animator.rotate_3d_enter, R.animator.rotate_3d_exit)
      var fragment = fragmentManager.findFragmentByTag(show)
      if (null == fragment) {
        fragment = RotateFragmentV11()
        val bundle = Bundle()
        bundle.putBoolean(EXTRA_DATA, isFront)
        fragment.arguments = bundle
        ft.add(containerId, fragment, show)
      } else {
        ft.show(fragment)
      }

      val hideFragment = fragmentManager.findFragmentByTag(hide)
      hideFragment?.also {
        ft.hide(it)
      }
      ft.commit()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater?,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater?.inflate(R.layout.fragment_rotate, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view?.apply {
      this.findViewById<TextView>(R.id.tvRotate).text = getString(R.string.fragment_v11)
      if (arguments?.getBoolean(EXTRA_DATA, true) != false) {
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.yellow))
      } else {
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
        //解决第二个fragment初始化时，直接覆盖在第一个上，导致动画异常
        //调用了setTransitionAlpha方法
        this.alpha = 0f
      }
    }
  }

//  private fun setTransitionAlpha(view: View, alpha: Float) {
//    try {
//      val clazz = Class.forName("android.support.transition.ViewUtils")
//      val method =
//        clazz.getDeclaredMethod("setTransitionAlpha", View::class.java, Float::class.java)
//      method.isAccessible = true
//      method.invoke(null, view, alpha)
//    } catch (e: Exception) {
//      Timber.e(e)
//    }
//  }

  override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator {
    return when (nextAnim) {
      R.animator.rotate_3d_exit -> AnimatorUtils.createExitAnimator()
      //因为进入动画为了和退出动画配合，设了一个延时，所以第一次进入的时候也有个延时，只是因为很短，目前忽略
      R.animator.rotate_3d_enter -> AnimatorUtils.createEnterAnimator().apply {
        addListener(object : AnimatorListener {
          override fun onAnimationRepeat(animation: Animator?) {
          }

          override fun onAnimationEnd(animation: Animator?) {
          }

          override fun onAnimationCancel(animation: Animator?) {
          }

          override fun onAnimationStart(animation: Animator?) {
            getTarget()?.alpha = 1f
          }
        })
      }
      else -> super.onCreateAnimator(transit, enter, nextAnim)
    }
  }
}