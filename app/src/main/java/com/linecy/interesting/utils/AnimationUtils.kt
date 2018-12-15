package com.linecy.interesting.utils

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.linecy.interesting.ui.weight.Rotate3dAnimation

/**
 * @author by linecy.
 */
object AnimationUtils {
  fun createEnterRotate3dAnimation(): Rotate3dAnimation {
    return Rotate3dAnimation(
      -90f,
      0f,
      0.5f,
      0.5f,
      0.5f,
      false,
      Rotate3dAnimation.ORIENTATION_Y,
      Rotate3dAnimation.TYPE_SCALE
    ).apply {
      duration = 600
      startOffset = 300//相对于开始时间，该动画应该何时开始
      fillAfter = false
      interpolator = DecelerateInterpolator()
    }
  }

  fun createExitRotate3dAnimation(): Rotate3dAnimation {
    return Rotate3dAnimation(
      0f,
      90f,
      0.5f,
      0.5f,
      0.5f,
      true,
      Rotate3dAnimation.ORIENTATION_Y,
      Rotate3dAnimation.TYPE_SCALE
    ).apply {
      duration = 300
      fillAfter = false
      interpolator = AccelerateInterpolator()
    }
  }
}