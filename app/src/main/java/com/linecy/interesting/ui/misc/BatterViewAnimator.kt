package com.linecy.interesting.ui.misc

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewAnimator

/**
 * @author by linecy.
 */
abstract class BatterViewAnimator(context: Context?, attrs: AttributeSet?) : ViewAnimator(context,
    attrs) {

  open fun setDisplayedChildId(id: Int) {
    if (id == getDisplayedChildId()) {
      return
    }
    val count = childCount
    for (i in 0 until count) {
      if (id == getChildAt(i).id) {
        displayedChild = i
        return
      }
    }
    val name = resources.getResourceEntryName(id)
    throw IllegalArgumentException("No view with ID $name")

  }


  protected fun getDisplayedChildId(): Int {
    return getChildAt(displayedChild).id
  }
}