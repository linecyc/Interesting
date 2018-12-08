package com.linecy.interesting.ui.misc

import android.Manifest
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Vibrator
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.View.MeasureSpec
import com.linecy.interesting.R
import com.linecy.interesting.ui.home.adapter.RecyclerViewAdapter.HeaderViewHolder

/**
 * @author by linecy.
 */
abstract class ItemTouchCallback(
  private val leftOptionView: View? = null,
  private val rightOptionView: View? = null,
  private val swipeRefreshLayout: SwipeRefreshLayout? = null
) :
  ItemTouchHelper.Callback() {

  private val maxSize = 200
  private var defaultBackground: Drawable? = null

  override fun getMovementFlags(p0: RecyclerView, p1: ViewHolder): Int {
    return if (p0.layoutManager is GridLayoutManager) {
      val dragFlag =
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
      ItemTouchHelper.Callback.makeMovementFlags(dragFlag, 0)
    } else {

      //HeaderViewHolder 是一个横向的recyclerView，会冲突,屏蔽掉
      val dragFlag =
        if (p1 is HeaderViewHolder) {
          0
        } else {
          ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
      val swipeFlag = if (p1 is HeaderViewHolder) {
        0
      } else if (leftOptionView != null && rightOptionView == null) {
        ItemTouchHelper.RIGHT
      } else if (rightOptionView != null && leftOptionView == null) {
        ItemTouchHelper.LEFT
      } else if (leftOptionView != null && rightOptionView != null) {
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
      } else {
        0
      }

      ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipeFlag)
    }
  }

  override fun onMove(p0: RecyclerView, p1: ViewHolder, p2: ViewHolder): Boolean {
    val from = p1.adapterPosition
    val to = p2.adapterPosition
    p0.adapter?.notifyItemMoved(from, to)
    return true
  }

  override fun onSwiped(p0: ViewHolder, p1: Int) {
    onItemRemoved(p0.adapterPosition)
  }

  /**
   * 解决swipeRefreshLayout 与 ItemTouchCallback手势冲突
   */
  override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
    defaultBackground = viewHolder?.itemView?.background
    when (actionState) {
      ItemTouchHelper.ACTION_STATE_DRAG -> {
        viewHolder?.itemView?.context?.let {
          if (ContextCompat.checkSelfPermission(
              it,
              Manifest.permission.VIBRATE
            ) == PermissionChecker.PERMISSION_GRANTED
          ) {
            val vibrator = it.getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(20)
          }
        }
        onSelected(viewHolder)
      }
      else -> {
        swipeRefreshLayout?.isEnabled = true
      }
    }
  }

  override fun onChildDraw(
    c: Canvas,
    recyclerView: RecyclerView,
    viewHolder: ViewHolder,
    dX: Float,
    dY: Float,
    actionState: Int,
    isCurrentlyActive: Boolean
  ) {
    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
      val rootView = viewHolder.itemView
      //dx>0 left
      //dx<0 right
      //想悬停的话actionState=0，isCurrentlyActive=false,固定value大小
      val value = when {
        dX > maxSize -> maxSize.toFloat()
        dX < -maxSize -> -maxSize.toFloat()
        else -> dX
      }
      if (VERSION.SDK_INT >= 21 && isCurrentlyActive) {
        val originalElevation = rootView.getTag(R.id.item_touch_helper_previous_elevation)
        if (originalElevation == null) {
          val oe = ViewCompat.getElevation(rootView)
          val newElevation = 1.0f + findMaxElevation(recyclerView, rootView)
          ViewCompat.setElevation(rootView, newElevation)
          rootView.setTag(R.id.item_touch_helper_previous_elevation, oe)
        }
      }
      rootView.translationX = value
      rootView.translationY = dY
    } else {
      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
  }

  private fun drawOptionView(
    itemView: View,
    canvas: Canvas,
    dX: Float,
    dY: Float,
    actionState: Int,
    isCurrentlyActive: Boolean
  ) {
    val value = when {

      dX > maxSize -> maxSize
      dX < -maxSize -> -maxSize
      else -> dX.toInt()
    }
    val width = MeasureSpec.makeMeasureSpec(Math.abs(value), MeasureSpec.EXACTLY)
    val height = MeasureSpec.makeMeasureSpec(Math.abs(itemView.height), MeasureSpec.EXACTLY)

    val left: Int
    val right: Int
    val tranX: Float
    val view: View?
    if (dX >= 0) {
      left = itemView.left
      right = itemView.left + value
      tranX = 0f
      view = leftOptionView
    } else {
      left = itemView.right + value
      right = itemView.right
      tranX = itemView.right + value.toFloat()
      view = rightOptionView
    }
    view?.run {
      measure(width, height)
      layout(left, itemView.top, right, itemView.bottom)
      canvas.save()
      canvas.translate(tranX, itemView.top.toFloat())
      draw(canvas)
      canvas.restore()
    }
  }

  override fun onChildDrawOver(
    c: Canvas,
    recyclerView: RecyclerView,
    viewHolder: ViewHolder?,
    dX: Float,
    dY: Float,
    actionState: Int,
    isCurrentlyActive: Boolean
  ) {
    viewHolder?.itemView?.let {
      drawOptionView(it, c, dX, dY, actionState, isCurrentlyActive)
    }
  }

  override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
    super.clearView(recyclerView, viewHolder)
    viewHolder.itemView.background = defaultBackground
  }

  private fun onSelected(viewHolder: ViewHolder?) {
    swipeRefreshLayout?.isEnabled = false
    viewHolder?.itemView?.run {
      setBackgroundColor(
        ContextCompat.getColor(
          this.context,
          R.color.blackTranslucent
        )
      )
    }
  }

  private fun findMaxElevation(recyclerView: RecyclerView, itemView: View): Float {
    val childCount = recyclerView.childCount
    var max = 0.0f
    for (i in 0 until childCount) {
      val child = recyclerView.getChildAt(i)
      if (child !== itemView) {
        val elevation = ViewCompat.getElevation(child)
        if (elevation > max) {
          max = elevation
        }
      }
    }
    return max
  }

  abstract fun onItemRemoved(position: Int)
}