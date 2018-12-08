package com.linecy.interesting.ui.misc

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.linecy.interesting.utils.GaussianBlurUtils
import java.security.MessageDigest

/**
 * @author by linecy.
 */
class GaussianBlurTransformation(
  private val context: Context,
  private val radius: Float = 10f
) : BitmapTransformation() {
  override fun updateDiskCacheKey(messageDigest: MessageDigest) {

  }

  override fun transform(
    pool: BitmapPool, toTransform: Bitmap, outWidth: Int,
    outHeight: Int
  ): Bitmap {
//    var result = pool.get(toTransform.width, toTransform.height, Bitmap.Config.ARGB_8888)
//    result = GaussianBlurUtils.renderScriptBlur(context, result, 25f)
//    val canvas = Canvas(result)
//    val paint = Paint()
//    paint.shader = BitmapShader(toTransform, TileMode.CLAMP, TileMode.CLAMP)
//    paint.isAntiAlias = true
//    val rectF = RectF(0f, 0f, toTransform.width.toFloat(), toTransform.height.toFloat())
//    canvas.drawRoundRect(rectF, radius, 0f, paint)
//    return result
    return GaussianBlurUtils.renderScriptBlur(context, toTransform, radius)

  }


}