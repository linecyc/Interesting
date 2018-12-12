package com.linecy.interesting.ui.weight

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.transition.TransitionValues
import android.transition.Visibility
import android.view.View
import android.view.ViewGroup

/**
 * @author by linecy.
 */


private const val PROPNAME_TRANSITION_SLIDE_BOTH_BOUNDS = "customtransition:slide_both_bounds"

class SlideExplode : Visibility() {
  private val outLocation = IntArray(2)


  /**
   * 为集合中的目标值添加对应的属性
   */
  private fun capture(transitionValues: TransitionValues?) {
    transitionValues?.run {
      view.getLocationOnScreen(outLocation)
      val left = outLocation[0]
      val top = outLocation[1]
      val right = left + view.width
      val bottom = top + view.height
      this.values[PROPNAME_TRANSITION_SLIDE_BOTH_BOUNDS] = Rect(left, top, right, bottom)
    }
  }

  override fun captureStartValues(transitionValues: TransitionValues?) {
    super.captureStartValues(transitionValues)
    capture(transitionValues)
  }

  override fun captureEndValues(transitionValues: TransitionValues?) {
    super.captureEndValues(transitionValues)
    capture(transitionValues)
  }

  override fun onAppear(
    sceneRoot: ViewGroup?,
    view: View?,
    startValues: TransitionValues?,
    endValues: TransitionValues?
  ): Animator? {
    return endValues?.let { values ->
      view?.let {
        val bounds = values.values[PROPNAME_TRANSITION_SLIDE_BOTH_BOUNDS] as Rect
        val endY = it.translationY
        val startY = endY + calculateDistance(sceneRoot, bounds)
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, endY)
      }
    }
  }


  override fun onDisappear(
    sceneRoot: ViewGroup?,
    view: View?,
    startValues: TransitionValues?,
    endValues: TransitionValues?
  ): Animator? {
    return startValues?.let { values ->
      view?.let {
        val bounds = values.values[PROPNAME_TRANSITION_SLIDE_BOTH_BOUNDS] as Rect
        val startY = it.translationY
        val endY = startY + calculateDistance(sceneRoot, bounds)
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, endY)
      }
    }
  }

  private fun calculateDistance(sceneRoot: ViewGroup?, bounds: Rect?): Int {
    return sceneRoot?.let {
      it.getLocationOnScreen(outLocation)
      val sceneRootY = outLocation[1]
      when (epicenter) {
        null -> -sceneRoot.height
        else -> {
          bounds?.run {
            when {
              this.top <= epicenter.top -> sceneRootY - epicenter.top
              else -> sceneRootY + sceneRoot.height - epicenter.bottom
            }
          } ?: 0
        }
      }
    } ?: 0
  }
}