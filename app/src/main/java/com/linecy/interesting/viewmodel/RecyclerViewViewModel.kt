package com.linecy.interesting.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.linecy.interesting.data.ListData
import com.linecy.interesting.data.Response
import java.lang.ref.WeakReference
import java.util.Random

/**
 * recyclerView 的viewModel.
 *
 * @author by linecy.
 */
class RecyclerViewViewModel : ViewModel() {

  val data = MutableLiveData<ListData>()
  private val handler = DataHandler(this)

  //val isRefreshing = MutableLiveData<Boolean>()
  val isRefreshing = ObservableBoolean(false)
  val viewStatus = ObservableInt()

  companion object {
    private const val MESSAGE = 11

  }

  fun refreshData() {
    isRefreshing.set(true)
    viewStatus.set(0)
    handler.sendEmptyMessageDelayed(
      MESSAGE, 3000
    )
  }


  override fun onCleared() {
    super.onCleared()
    handler.removeMessages(MESSAGE)
  }

  private class DataHandler(recyclerViewViewModel: RecyclerViewViewModel) :
    Handler(Looper.getMainLooper()) {
    private val weakReference = WeakReference<RecyclerViewViewModel>(recyclerViewViewModel)

    override fun handleMessage(msg: Message?) {
      val listViewModel = weakReference.get()
      listViewModel?.let {
        if (msg?.what == MESSAGE) {
          val random = Random()
          val status = random.nextInt(100)
          when {
            status < 10 -> {
              //error
              it.data.value = null
              it.isRefreshing.set(false)
              it.viewStatus.set(-1)
            }
            status in 11..20 -> {
              //empty
              it.data.value = null
              it.isRefreshing.set(false)
              it.viewStatus.set(2)
            }
            else -> {
              //has data
              val size = 1 + random.nextInt(20)
              val list = Response.createListData(size)
              it.data.value = ListData(
                header = Response.createHeaderData(),
                item = list
              )
              it.isRefreshing.set(false)
              it.viewStatus.set(1)
            }
          }

        }
      }
    }
  }
}