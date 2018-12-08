package com.linecy.interesting.utils

import android.content.Context
import android.util.TypedValue

/**
 * @author by linecy.
 */
object DisplayUtils {


  /**
   * dp转px
   */
  fun dp2px(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      dp,
      context.resources.displayMetrics
    )
  }

  /**
   * px转dp
   */
  fun px2dp(context: Context, px: Float): Float {
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_PX,
      px,
      context.resources.displayMetrics
    )
  }
}