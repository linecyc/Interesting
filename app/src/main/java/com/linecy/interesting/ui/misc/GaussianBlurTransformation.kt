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
    return GaussianBlurUtils.renderScriptBlur(context, toTransform, radius)

  }


}