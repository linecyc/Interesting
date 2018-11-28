package com.linecy.interesting.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.linecy.interesting.data.ListData
import com.linecy.interesting.data.ListItem
import java.lang.ref.WeakReference
import java.util.Random

/**
 * @author by linecy.
 */
class ListViewModel : ViewModel() {

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
        MESSAGE, 3000)
  }


  override fun onCleared() {
    super.onCleared()
    handler.removeMessages(MESSAGE)
  }

  private class DataHandler(listViewModel: ListViewModel) : Handler(Looper.getMainLooper()) {
    private val weakReference = WeakReference<ListViewModel>(listViewModel)

    override fun handleMessage(msg: Message?) {
      val listViewModel = weakReference.get()
      listViewModel?.let {
        if (msg?.what == MESSAGE) {
          val random = Random()
          val status = random.nextInt(100)

          when {
            status < 33 -> {
              //error
              it.data.value = null
              it.isRefreshing.set(false)
              it.viewStatus.set(-1)
            }
            status < 66 -> {
              //empty
              it.data.value = null
              it.isRefreshing.set(false)
              it.viewStatus.set(2)
            }
            else -> {
              //has data
              val size = 1 + random.nextInt(20)
              val list = it.createListData(size)
              it.data.value = ListData(
                  url = "https://images.unsplash.com/photo-1511821899780-d67e9f638fea?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=fb4d4a727ea29e5a32dbdd7ea6ed5cd1&auto=format&fit=crop&w=948&q=80",
                  item = list)
              it.isRefreshing.set(false)
              it.viewStatus.set(1)
            }
          }

        }
      }
    }
  }


  private fun createListData(size: Int): List<ListItem> {
    val list = ArrayList<ListItem>(size)
    for (i in 0 until size) {
      list.add(ListItem(
          url = "https://images.unsplash.com/photo-1530991671072-ac4f81c2c3c1?ixlib=rb-0.3.5&s=f61f7310161b34d4fc79be9a09ac8d8b&auto=format&fit=crop&w=1567&q=80",
          name = "第${i + 1}个你敢信？？", detail = "这是第${i + 1}个的描述，一定要长长长长长长长长长长长长长长长~~"))
    }
    return list
  }

}