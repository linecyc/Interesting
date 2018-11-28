package com.linecy.interesting.ui.misc

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableInt
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.linecy.interesting.R
import com.linecy.interesting.R.id.empty
import com.linecy.interesting.R.id.error
import kotlinx.android.synthetic.main.layout_empty.view.empty
import kotlinx.android.synthetic.main.layout_error.view.error
import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * @author by linecy.
 */

const val STATUS_DEFAULT = 0
const val STATUS_CONTENT = 1
const val STATUS_EMPTY = 2
const val STATUS_ERROR = -1
private const val TAG = "ViewContainer"

class ViewContainer(context: Context, attrs: AttributeSet?) : BatterViewAnimator(context, attrs) {


  @IntDef(STATUS_DEFAULT, STATUS_EMPTY, STATUS_CONTENT, STATUS_ERROR)
  @Retention(SOURCE)
  annotation class ViewStatus

  private var emptyCallback: EmptyCallback? = null
  private var errorCallback: ErrorCallback? = null
  private var contentId = 0


  init {
    View.inflate(context, R.layout.view_container, this)
    readAttrs(context, attrs)
  }


  private fun readAttrs(context: Context, attrs: AttributeSet?) {
    val a = context.obtainStyledAttributes(attrs, R.styleable.ViewContainer)
    contentId = a.getResourceId(R.styleable.ViewContainer_displayedChildId, 0)
    a.recycle()
  }

  override fun setDisplayedChildId(id: Int) {
    super.setDisplayedChildId(id)
    when (id) {
      R.id.error -> {
        errorCallback?.run {
          error.setOnClickListener {
            error.visibility = View.GONE
            onShowError()
          }
        }
      }
      R.id.empty -> {
        emptyCallback?.run {
          empty.setOnClickListener {
            empty.visibility = View.GONE
            onShowEmpty()
          }
        }
      }
    }
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    empty.visibility = View.GONE
    error.visibility = View.GONE
  }


  fun setEmptyCallback(callback: EmptyCallback) {
    this.emptyCallback = callback
  }

  fun setErrorCallback(callback: ErrorCallback) {
    this.errorCallback = callback
  }

  interface EmptyCallback {
    fun onShowEmpty()
  }


  interface ErrorCallback {
    fun onShowError()
  }


  companion object {

    @JvmStatic
    @BindingAdapter("displayedChildStatus")
    fun setDisplayedChildStatus(viewContainer: ViewContainer, status: ObservableInt) {
      when (status.get()) {
        STATUS_DEFAULT -> {
        }
        STATUS_EMPTY -> {
          viewContainer.setDisplayedChildId(empty)
        }
        STATUS_CONTENT -> {
          if (0 != viewContainer.contentId) {
            viewContainer.setDisplayedChildId(viewContainer.contentId)
            Log.e(TAG, "The content id is ${viewContainer.contentId}.")
          } else {
            Log.e(TAG, "The content id must be set.")
          }
        }
        else -> {
          viewContainer.setDisplayedChildId(error)
        }
      }
    }
  }
}