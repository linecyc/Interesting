package com.linecy.interesting.ui.transition.rotate

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import com.linecy.interesting.R
import com.linecy.interesting.utils.AnimationUtils
import com.linecy.interesting.utils.AnimatorUtils

/**
 * Animator 与Animation的执行是不同的(moveFragmentToExpectedState())
 *
 *
 * v11的FragmentManager:fragment add后，动画执行前会设置alpha=1
 * v4的FragmentManager:fragment add后，动画执行前会设置alpha=0
 *
 * 所以Animator在第二个fragment show，第一个hide的时候，此时第二个才add，这个时候就会导致第二个
 * fragment会在第一个fragment上面，导致动画重叠，异常显示，因此设置alpha=0
 * {@link com.linecy.interesting.ui.transition.rotate.RotateFragmentV11}
 *
 * @author by linecy.
 */

class RotateFragmentV4 @SuppressLint("ValidFragment")
private constructor() : Fragment() {

  companion object {

    private const val TAG_FRAGMENT1 = "show1"
    private const val TAG_FRAGMENT2 = "show2"
    private const val EXTRA_DATA = "extra_data"

    fun rotate(
      fragmentManager: FragmentManager, @IdRes containerId: Int,
      isFront: Boolean
    ) {
      val show = if (isFront) TAG_FRAGMENT1 else TAG_FRAGMENT2
      val hide = if (isFront) TAG_FRAGMENT2 else TAG_FRAGMENT1
      val ft = fragmentManager.beginTransaction()
      ft.setCustomAnimations(R.anim.rotate_3d_enter, R.anim.rotate_3d_exit)
      //ft.setCustomAnimations(R.animator.rotate_3d_enter, R.animator.rotate_3d_exit)
      var fragment = fragmentManager.findFragmentByTag(show)
      if (null == fragment) {
        fragment = RotateFragmentV4()
        val bundle = Bundle()
        bundle.putBoolean(EXTRA_DATA, isFront)
        fragment.arguments = bundle
        ft.add(containerId, fragment, show)
      } else {
        ft.show(fragment)
      }

      val hideFragment = fragmentManager.findFragmentByTag(hide)
      hideFragment?.run {
        ft.hide(this)
      }
      ft.commit()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_rotate, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.apply {
      this.findViewById<TextView>(R.id.tvRotate).text = getString(R.string.fragment_v4)
      if (arguments?.getBoolean(EXTRA_DATA, true) != false) {
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.red))
      } else {
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
      }
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

  override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator {
    return when (nextAnim) {
      R.animator.rotate_3d_exit -> AnimatorUtils.createExitAnimator()
      R.animator.rotate_3d_enter -> AnimatorUtils.createEnterAnimator()
      else -> super.onCreateAnimator(transit, enter, nextAnim)
    }
  }

}