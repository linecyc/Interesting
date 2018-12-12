package com.linecy.interesting.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.data.Response
import java.lang.ref.WeakReference
import java.util.Random

/**
 * transition 的viewModel.
 *
 * @author by linecy.
 */
class TransitionViewModel : ViewModel() {

  val data = ObservableField<List<ListItem>>()
  private val handler = DataHandler(this)

  val isRefreshing = ObservableBoolean(false)
  val viewStatus = ObservableInt()

  companion object {
    private const val MESSAGE = 12

  }

  fun refreshData() {
    isRefreshing.set(true)
    viewStatus.set(0)
    handler.sendEmptyMessageDelayed(
      MESSAGE, 2500
    )
  }


  override fun onCleared() {
    super.onCleared()
    handler.removeMessages(MESSAGE)
  }

  private class DataHandler(viewModel: TransitionViewModel) :
    Handler(Looper.getMainLooper()) {
    private val weakReference = WeakReference<TransitionViewModel>(viewModel)

    override fun handleMessage(msg: Message?) {
      val listViewModel = weakReference.get()
      listViewModel?.let {
        if (msg?.what == MESSAGE) {
          val random = Random()
          val status = random.nextInt(100)
          when {
            status < 10 -> {
              //error
              it.data.set(null)
              it.isRefreshing.set(false)
              it.viewStatus.set(-1)
            }
            status in 11..20 -> {
              //empty
              it.data.set(null)
              it.isRefreshing.set(false)
              it.viewStatus.set(2)
            }
            else -> {
              //has data
              val list = Response.createTransitionData()
              it.data.set(list)
              it.isRefreshing.set(false)
              it.viewStatus.set(1)
            }
          }
        }
      }
    }
  }
}