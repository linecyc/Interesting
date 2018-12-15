package com.linecy.interesting.ui.weight

import android.animation.ValueAnimator
import android.view.View

/**
 * @author by linecy.
 */
class Rotate3dAnimator(
  private val fromDegrees: Float,
  private val toDegrees: Float,
  private var centerX: Float,
  private var centerY: Float,
  private var depthZ: Float,
  private val reverse: Boolean,
  private val rotateOrientation: Int = ORIENTATION_X,
  val type: Int = TYPE_PX
) : ValueAnimator(), ValueAnimator.AnimatorUpdateListener {

  companion object {
    const val TYPE_SCALE = 0
    const val TYPE_PX = 1
    const val ORIENTATION_X = 0
    const val ORIENTATION_Y = 1
  }

  private var targetView: View? = null

  init {
    this.addUpdateListener(this)
    setFloatValues(0f, 1f)
  }

  fun getTarget() = targetView

  override fun setTarget(target: Any?) {
    super.setTarget(target)
    if (null == target) {
      throw NullPointerException("Target can't be null.")
    }
    targetView = target as View
    targetView?.let {
      if (type == TYPE_SCALE) {
        centerX *= it.width
        centerY *= it.height
      }
    }
  }

  override fun onAnimationUpdate(animation: ValueAnimator?) {
    animation?.run {
      val progress = this.animatedValue as Float
      val degrees = fromDegrees + (toDegrees - fromDegrees) * progress
      val value = depthZ * Math.sqrt(2.0)
      targetView?.let {
        if (reverse) {
          //progress 0-1
          it.scaleX = (1 - (1 - value) * progress).toFloat()
          it.scaleY = (1 - (1 - value) * progress).toFloat()
        } else {
          //progress 0-1
          it.scaleX = (value + (1 - value) * progress).toFloat()
          it.scaleY = (value + (1 - value) * progress).toFloat()
        }
        if (rotateOrientation == ORIENTATION_Y) {
          it.rotationY = degrees
        } else {
          it.rotationX = degrees
        }
      }
    }
  }

  var view: Int = 0


}