package com.linecy.interesting.ui.misc

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader.TileMode.CLAMP
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author by linecy.
 */
class RoundTransformation(radius: Float = 5f) : BitmapTransformation() {

  private var radius: Float = 0f

  init {
    this.radius = Resources.getSystem().displayMetrics.density * radius
    //this.radius = DisplayUtils.dp2px(g)
  }

  override fun updateDiskCacheKey(messageDigest: MessageDigest) {

  }

  override fun transform(
    pool: BitmapPool,
    toTransform: Bitmap,
    outWidth: Int,
    outHeight: Int
  ): Bitmap {
    val result = pool.get(toTransform.width, toTransform.height, Bitmap.Config.ARGB_8888)
    val c = Canvas(result)
    val paint = Paint()
    paint.shader = BitmapShader(toTransform, CLAMP, CLAMP)
    paint.isAntiAlias = true
    val rectF = RectF(0f, 0f, toTransform.width.toFloat(), toTransform.height.toFloat())
    c.drawRoundRect(rectF, radius, radius, paint)
    return result
  }

}