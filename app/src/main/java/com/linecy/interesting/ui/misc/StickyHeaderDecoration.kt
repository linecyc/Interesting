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
import android.view.ViewGroup

/**
 * 粘性头部，针对item的头部，并且item的头部上面还有一个banner.
 * 类似ConstraintLayout 收折banner，头部固定在顶部的效果
 * 默认不绘制分割线
 *
 * @author by linecy.
 */
class StickyHeaderDecoration private constructor(builder: Builder) : RecyclerView.ItemDecoration() {

  private val headerView: View
  private var startingPosition: Int = 1
  private var drawDecoration: Boolean = false
  private var decorationHeight: Int = 2
  private var decorationMarginLeft: Int = 0
  private var decorationMarginRight: Int = 0


  private var stickyHeight: Int = 0//头部高
  private var paint: Paint? = null

  companion object {
    @JvmStatic
    fun builder(): Builder {
      return Builder()
    }
  }

  init {
    headerView = builder.getHeaderView() ?:
        throw IllegalArgumentException("The headerView must be not null.")
    startingPosition = builder.getStartingPosition()
    drawDecoration = builder.getDrawDecoration()
    decorationHeight = builder.getDecorationHeight()
    decorationMarginLeft = builder.getDecorationMarginLeft()
    decorationMarginRight = builder.getDecorationMarginRight()
    if (startingPosition < 0) {
      throw IllegalArgumentException("The start position must be greater than 0.")
    }
    if (drawDecoration) {
      paint = Paint(Paint.ANTI_ALIAS_FLAG)
      if (decorationHeight <= 0) {
        throw IllegalArgumentException("The decoration size must be greater than 0.")
      }
      paint?.run {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = builder.getDecorationColor()

      }
    }
  }

  override fun getItemOffsets(
    outRect: Rect, view: View,
    parent: RecyclerView, state: RecyclerView.State
  ) {
    measureHeader(parent)
    val pos = parent.getChildAdapterPosition(view)
    when {
      pos < startingPosition -> outRect.set(0, 0, 0, 0)
      pos == startingPosition -> {
        outRect.top = stickyHeight
        outRect.bottom = decorationHeight
      }
      drawDecoration && pos > startingPosition -> outRect.set(0, 0, 0, decorationHeight)
    }
  }

  /**
   * 为startingPosition之后，最后一个之前画分割线
   */
  override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
    if (drawDecoration) {
      paint?.let {
        val left = parent.paddingLeft.toFloat() + decorationMarginLeft
        val right = parent.measuredWidth - parent.paddingRight.toFloat() - decorationMarginRight
        val count = parent.childCount
        for (i in 0 until count) {
          val child = parent.getChildAt(i)
          //大于等于起始位置才才是绘制分割线，头部不需要
          if (parent.getChildAdapterPosition(child) >= startingPosition) {
            val lp: LayoutParams = child.layoutParams as LayoutParams
            val top = child.bottom + lp.bottomMargin.toFloat()
            val bottom = top + decorationHeight
            c.drawRect(left, top, right, bottom, it)
          }
        }
      }
    }
  }

  override fun onDrawOver(
    c: Canvas, parent: RecyclerView,
    state: RecyclerView.State
  ) {
    val childCount = parent.childCount
    val left = (parent.left + parent.paddingLeft).toFloat()
    for (i in 0 until childCount) {
      val child = parent.getChildAt(i)
      val lp = child.layoutParams as RecyclerView.LayoutParams
      val top = child.top - lp.topMargin
      val pos = parent.getChildAdapterPosition(child)
      //第startingPosition位置以后才偏移，因为前面是不需要的
      if (pos == startingPosition) {
        //到顶之后不偏移
        if (top <= stickyHeight) {
          drawHeader(parent, c)
        } else {
          c.save()
          c.translate(left, (top - stickyHeight).toFloat())//移动到预留位置
          drawHeader(parent, c)
          c.restore()
        }
      } else if (pos > startingPosition) {
        //到顶之后不偏移,且只绘制顶部一个
        if (top <= stickyHeight) {
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
      headerView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )
      childWidth = headerView.measuredWidth
    } else {
      childWidth = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
    }
    val childHeight: Int = if (headerView.layoutParams.height > 0) {
      View.MeasureSpec.makeMeasureSpec(
        headerView.layoutParams.height,
        View.MeasureSpec.EXACTLY
      )
    } else {
      View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)//未指定
    }
    headerView.measure(childWidth, childHeight)
    headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    stickyHeight = headerView.measuredHeight
//    val width = MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY)
//    headerView.measure(width, headerView.measuredHeight)
//    headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
//    stickyHeight = headerView.measuredHeight
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


  @Suppress("unused")
  class Builder {
    private var headerView: View? = null
    private var startingPosition: Int = 1
    private var drawDecoration: Boolean = false
    private var decorationHeight: Int = 2
    private var decorationColor: Int = Color.BLACK
    private var decorationMarginLeft: Int = 0
    private var decorationMarginRight: Int = 0


    fun getHeaderView(): View? {
      return headerView
    }

    fun getStartingPosition(): Int {
      return startingPosition
    }

    fun getDrawDecoration(): Boolean {
      return drawDecoration
    }

    fun getDecorationHeight(): Int {
      return decorationHeight
    }

    fun getDecorationColor(): Int {
      return decorationColor
    }

    fun getDecorationMarginLeft(): Int {
      return decorationMarginLeft
    }

    fun getDecorationMarginRight(): Int {
      return decorationMarginRight
    }

    fun headerView(headerView: View): Builder {
      this.headerView = headerView
      return this
    }

    fun startingPosition(startingPosition: Int): Builder {
      this.startingPosition = startingPosition
      return this
    }

    fun drawDecoration(drawDecoration: Boolean): Builder {
      this.drawDecoration = drawDecoration
      return this
    }

    fun decorationHeight(decorationHeight: Int): Builder {
      this.decorationHeight = decorationHeight
      return this
    }

    fun decorationColor(@ColorInt decorationColor: Int): Builder {
      this.decorationColor = decorationColor
      return this
    }

    fun decorationMarginLeft(marginLeft: Int): Builder {
      this.decorationMarginLeft = marginLeft
      return this
    }

    fun decorationMarginRight(marginRight: Int): Builder {
      this.decorationMarginRight = marginRight
      return this
    }

    fun build(): StickyHeaderDecoration {
      return StickyHeaderDecoration(this)
    }
  }
}
