package com.linecy.interesting.ui.misc

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader.TileMode.CLAMP
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author by linecy.
 */
class CircleTransformation : BitmapTransformation() {

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
    val radius = if (toTransform.width >= toTransform.height) {
      toTransform.height / 2f
    } else {
      toTransform.width / 2f
    }
    c.drawCircle(toTransform.width / 2f, toTransform.height / 2f, radius, paint)
    return result
  }

}