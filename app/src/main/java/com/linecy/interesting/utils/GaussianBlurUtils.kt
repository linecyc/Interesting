package com.linecy.interesting.utils

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

/**
 * @author by linecy.
 */
object GaussianBlurUtils {


  /**
   * 高斯模糊
   *
   * @param context context
   * @param bitmap 需要模糊的数据源
   * @param radius 模糊的半径(越大越模糊，0-25)
   *
   * 适用于api>=17
   * 可以用扩展包@see{v8.renderscript}，适用于api>=9的
   * @see{v8.renderscript}
   */
  fun renderScriptBlur(context: Context, bitmap: Bitmap, radius: Float = 10f): Bitmap {
    val rs = RenderScript.create(context)

    val input = Allocation.createFromBitmap(rs, bitmap)
    val output = Allocation.createTyped(rs, input.type)

    val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    scriptIntrinsicBlur.setInput(input)
    scriptIntrinsicBlur.setRadius(radius)
    scriptIntrinsicBlur.forEach(output)
    output.copyTo(bitmap)
    rs.destroy()
    return bitmap
  }
}