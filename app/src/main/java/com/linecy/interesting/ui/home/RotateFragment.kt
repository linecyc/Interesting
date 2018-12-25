package com.linecy.interesting.ui.home

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.animation.Animation
import com.linecy.interesting.R
import com.linecy.interesting.ui.BaseFragment
import com.linecy.interesting.utils.AnimationUtils

/**
 * @author by linecy.
 */
class RotateFragment : BaseFragment<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return R.layout.fragment_rotation_vector
  }

  override fun onInitView(savedInstanceState: Bundle?) {
  }

  override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
    return when (nextAnim) {
      R.anim.rotate_3d_exit -> AnimationUtils.createExitRotate3dAnimation()
      //因为进入动画为了和退出动画配合，设了一个延时，所以第一次进入的时候也有个延时，只是因为很短，目前忽略
      R.anim.rotate_3d_enter -> AnimationUtils.createEnterRotate3dAnimation()
      else -> super.onCreateAnimation(transit, enter, nextAnim)
    }
  }
}