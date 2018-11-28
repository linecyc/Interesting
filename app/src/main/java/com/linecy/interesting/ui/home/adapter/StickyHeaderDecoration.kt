package com.linecy.interesting.ui.home.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * 粘性头部，针对item的头部，并且item的头部上面还有一个banner.
 * 类似ConstraintLayout 收折banner，头部固定在顶部的效果
 *
 * @author by linecy.
 */
class StickyHeaderDecoration(private val headerView: View,
    private val startingPosition: Int = 1) : RecyclerView.ItemDecoration() {

  private var headerHeight: Int = 0//头部高

  init {
    if (startingPosition < 0) {
      throw IllegalArgumentException("The start position must be more than 0.")
    }
  }

  override fun getItemOffsets(outRect: Rect, view: View,
      parent: RecyclerView, state: RecyclerView.State) {
    super.getItemOffsets(outRect, view, parent, state)
    measureHeader(parent)
    val pos = parent.getChildAdapterPosition(view)
    if (pos == startingPosition) {
      outRect.top = headerHeight
    }
  }

  override fun onDrawOver(c: Canvas, parent: RecyclerView,
      state: RecyclerView.State) {
    super.onDrawOver(c, parent, state)
    val childCount = parent.childCount
    val left = parent.left + parent.paddingLeft
    for (i in 0 until childCount) {
      val child = parent.getChildAt(i)
      val lp = child.layoutParams as RecyclerView.LayoutParams
      val top = child.top - lp.topMargin
      val pos = parent.getChildAdapterPosition(child)
      //第startingPosition位置以后才偏移，因为前面是不需要的
      if (pos == startingPosition) {
        //到顶之后不偏移
        if (top <= headerHeight) {
          drawHeader(parent, c)
        } else {
          c.save()
          c.translate(left.toFloat(), (top - headerHeight).toFloat())//移动到预留位置
          drawHeader(parent, c)
          c.restore()
        }
      } else if (pos > startingPosition) {
        //到顶之后不偏移
        if (top <= 0) {
          drawHeader(parent, c)
        }
      }
    }
  }

  /**
   * 测量头部
   */
  private fun measureHeader(parent: RecyclerView) {
    val childWidth: Int
    if (headerView.layoutParams == null) {
      headerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT)
      childWidth = headerView.measuredWidth
    } else {
      childWidth = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
    }
    val childHeight: Int = if (headerView.layoutParams.height > 0) {
      View.MeasureSpec.makeMeasureSpec(headerView.layoutParams.height,
          View.MeasureSpec.EXACTLY)
    } else {
      View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)//未指定
    }
    headerView.measure(childWidth, childHeight)
    headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    headerHeight = headerView.measuredHeight
  }

  /**
   * 绘制头
   *
   * @param parent recyclerView
   * @param c canvas
   */
  private fun drawHeader(parent: RecyclerView, c: Canvas) {
    updateHeaderView(headerView)//填充数据
    measureHeader(parent)//填充数据后重新测量View
    headerView.draw(c)
  }

  /**
   * 自定义header需要自己更新布局
   *
   * @param headerView 头部
   */
  private fun updateHeaderView(headerView: View) {
    //empty
  }
}
