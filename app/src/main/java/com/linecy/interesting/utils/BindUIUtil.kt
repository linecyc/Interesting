package com.linecy.interesting.utils

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.databinding.ObservableBoolean
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.linecy.interesting.ui.home.adapter.ListAdapter
import com.linecy.interesting.data.ListData

/**
 * @author by linecy.
 */

object BindUIUtil {


  @JvmStatic
  @BindingAdapter("loadUrl")
  fun loadUrl(imageView: ImageView, url: String?) {
    url?.run {
      Glide.with(imageView).load(this).into(imageView)
    }
  }

  @JvmStatic
  @BindingAdapter("loadAdapter")
  fun loadAdapter(recyclerView: RecyclerView, mu: MutableLiveData<ListData>) {
    val adapter = recyclerView.adapter
    if (adapter is ListAdapter) {
      adapter.refreshData(mu.value?.url, mu.value?.item)
    }

  }

  @JvmStatic
  @BindingAdapter("loading")
  fun loading(swipeRefreshLayout: SwipeRefreshLayout, mu: MutableLiveData<Boolean>) {
    swipeRefreshLayout.isRefreshing = mu.value ?: false
  }

  @JvmStatic
  @BindingAdapter("loading")
  fun loading(swipeRefreshLayout: SwipeRefreshLayout, bool: ObservableBoolean) {
    swipeRefreshLayout.isRefreshing = bool.get()
  }
}
