package com.linecy.interesting.ui.misc

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutParams
import android.support.v7.widget.RecyclerView.State
import android.view.View

/**
 *
 * @author by linecy.
 */
class LinearItemDecoration(
  private val orientation: Int = VERTICAL,
  private val decorationSize: Int = 2,
  @ColorInt private val decorationColor: Int = Color.YELLOW,
  private val decorationMarginStart: Int = 0,
  private val decorationMarginEnd: Int = 0

) :
  RecyclerView.ItemDecoration() {

  private var paint: Paint? = null

  companion object {
    const val HORIZONTAL = 0
    const val VERTICAL = 1
  }

  init {
    if (orientation != HORIZONTAL && orientation != VERTICAL) {
      throw IllegalArgumentException("The orientation is HORIZONTAL or VERTICAL.")
    }
    if (decorationSize <= 0) {
      throw IllegalArgumentException("The decoration size must be greater than 0.")
    }
    paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint?.run {
      isAntiAlias = true
      style = Paint.Style.FILL
      color = decorationColor

    }
  }

  override fun getItemOffsets(
    outRect: Rect, view: View,
    parent: RecyclerView, state: RecyclerView.State
  ) {
    if (orientation == VERTICAL) {
      outRect.bottom = decorationSize
    } else {
      outRect.right = decorationSize
    }
  }

  /**
   * 为startingPosition之后，最后一个之前画分割线
   */
  override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
    if (orientation == VERTICAL) {
      drawVertical(c, parent)
    } else {
      drawHorizontal(c, parent)
    }
  }

  private fun drawVertical(c: Canvas, parent: RecyclerView) {
    paint?.let {
      val left = parent.paddingLeft.toFloat() + decorationMarginStart
      val right = parent.measuredWidth - parent.paddingRight.toFloat() - decorationMarginEnd
      val count = parent.childCount
      for (i in 0 until count) {
        val child = parent.getChildAt(i)
        val lp: LayoutParams = child.layoutParams as LayoutParams
        val top = child.bottom + lp.bottomMargin.toFloat()
        val bottom = top + decorationSize
        c.drawRect(left, top, right, bottom, it)
      }
    }
  }

  private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
    paint?.let {
      val top = parent.paddingTop.toFloat() + decorationMarginStart
      val bottom = parent.measuredHeight.toFloat() - parent.paddingBottom - decorationMarginEnd
      val count = parent.childCount
      for (i in 0 until count) {
        val child = parent.getChildAt(i)
        val lp = child.layoutParams as LayoutParams
        val left = child.right + lp.rightMargin.toFloat()
        val right = left + decorationSize
        c.drawRect(left, top, right, bottom, it)
      }
    }
  }
}
