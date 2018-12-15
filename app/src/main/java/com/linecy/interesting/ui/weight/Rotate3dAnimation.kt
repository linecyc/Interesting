package com.linecy.interesting.ui.weight

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Google api demo Rotate3dAnimation.
 *
 * @author by linecy.
 */

class Rotate3dAnimation(
  private val fromDegrees: Float,
  private val toDegrees: Float,
  private var centerX: Float,
  private var centerY: Float,
  private var depthZ: Float,
  private val reverse: Boolean,
  private val rotateOrientation: Int = ORIENTATION_X,
  val type: Int = TYPE_PX
) :
  Animation() {

  companion object {
    const val TYPE_SCALE = 0
    const val TYPE_PX = 1
    const val ORIENTATION_X = 0
    const val ORIENTATION_Y = 1
  }

  private val camera = lazy { Camera() }

  override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
    super.initialize(width, height, parentWidth, parentHeight)
    if (type == TYPE_SCALE) {
      centerX *= width
      centerY *= height
      depthZ *= if (rotateOrientation == ORIENTATION_Y) width else height
    }
  }

  override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
    t?.let {
      val degrees = fromDegrees + (toDegrees - fromDegrees) * interpolatedTime
      val matrix = it.matrix
      with(camera.value) {
        this.save()
        if (reverse) {
          this.translate(0f, 0f, depthZ * interpolatedTime)
        } else {
          this.translate(0f, 0f, depthZ * (1 - interpolatedTime))
        }
        if (rotateOrientation == ORIENTATION_X) {
          this.rotateX(degrees)
        } else {
          this.rotateY(degrees)
        }
        this.getMatrix(matrix)
        this.restore()
      }
      matrix.preTranslate(-centerX, -centerY)
      matrix.postTranslate(centerX, centerY)
    } ?: super.applyTransformation(interpolatedTime, t)
  }

}