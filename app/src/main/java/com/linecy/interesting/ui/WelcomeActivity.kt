package com.linecy.interesting.ui

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.linecy.interesting.R
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.utils.DisplayUtils
import kotlinx.android.synthetic.main.activity_welcome.imageSwitcher
import kotlinx.android.synthetic.main.activity_welcome.indicatorLayout

/**
 * @author by linecy.
 */
class WelcomeActivity : BaseActivity<ViewDataBinding>() {

  //private val list = listOf(Constants.PHOTO1_URL, Constants.PHOTO4_URL, Constants.PHOTO7_URL)
  private val list = listOf(
    R.drawable.ic_circle_red,
    R.drawable.ic_circle_yellow,
    R.drawable.ic_circle_blue,
    R.drawable.ic_circle_green
  )

  private lateinit var gestureDetector: GestureDetector
  private var position = 0

  override fun layoutResId(): Int {
    return R.layout.activity_welcome
  }

  override fun onInitView(savedInstanceState: Bundle?) {

    showSystemBottomNavigation()
    hideBottomAppBar()
    gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
      override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
      ): Boolean {
        if (e1 != null && e2 != null) {
          fling(e1.x - e2.x > 100)
          return false
        }
        return super.onFling(e1, e2, velocityX, velocityY)
      }
    })

    for (i in 1..list.size) {
      indicatorLayout.addView(ImageView(this).apply {
        setPadding(10, 10, 10, 10)
        layoutParams = LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
          this.width = DisplayUtils.dp2px(this@WelcomeActivity, 16f).toInt()
          this.height = this.width
        }
      })
    }
    setCurrent(position)
    imageSwitcher.run {
      setFactory {
        val image = ImageView(this@WelcomeActivity)
        image.layoutParams = FrameLayout.LayoutParams(
          FrameLayout.LayoutParams.MATCH_PARENT,
          FrameLayout.LayoutParams.MATCH_PARENT
        )
        image
      }
      setImageResource(list[position])
    }
  }


  private fun setCurrent(position: Int) {
    val count = indicatorLayout.childCount
    for (i in 0 until count) {
      if (i == position) {
        when (i) {
          0 -> (indicatorLayout.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_circle_red)
          1 -> (indicatorLayout.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_circle_yellow)
          2 -> (indicatorLayout.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_circle_blue)
          3 -> (indicatorLayout.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_circle_green)
        }
      } else {
        (indicatorLayout.getChildAt(i) as ImageView).setImageResource(R.drawable.ic_circle_gray)
      }
    }
  }

  private fun fling(isRight: Boolean) {
    if (isRight) {
      if (position < list.size - 1) {
        imageSwitcher.setInAnimation(this, R.anim.trans_right_in)
        imageSwitcher.setOutAnimation(this, R.anim.trans_left_out)
        position++
        imageSwitcher.setImageResource(list[position])
        setCurrent(position)
      } else {
        //跳转到主页
        Navigator.navigateToHome(this)
        finish()
        overridePendingTransition(0, R.anim.trans_left_out)//放在前面也可以？？
      }
    } else {
      if (position > 0) {
        imageSwitcher.setInAnimation(this, R.anim.trans_left_in)
        imageSwitcher.setOutAnimation(this, R.anim.trans_right_out)
        position--
        imageSwitcher.setImageResource(list[position])
        setCurrent(position)
      }
    }
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    return gestureDetector.onTouchEvent(event)
  }
}

