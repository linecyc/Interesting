package com.linecy.interesting.utils

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.linecy.interesting.data.ListData
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.ui.adapter.CustomAdapter
import com.linecy.interesting.ui.home.adapter.HeaderViewAdapter
import com.linecy.interesting.ui.home.adapter.RecyclerViewAdapter
import com.linecy.interesting.ui.home.adapter.TransitionAdapter
import com.linecy.interesting.ui.misc.GaussianBlurTransformation
import com.linecy.interesting.ui.misc.RoundTransformation

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
  @BindingAdapter("loadRoundUrl")
  fun loadRoundUrl(imageView: ImageView, url: String?) {
    url?.let {
      Glide.with(imageView).load(it).apply(RequestOptions.bitmapTransform(RoundTransformation()))
        .into(imageView)
    }
  }

  @JvmStatic
  @BindingAdapter("setBlurPhoto")
  fun setBlurPhoto(imageView: ImageView, url: String?) {
    setBlurPhoto(imageView, url, 10f)
  }

  @JvmStatic
  fun setBlurPhoto(imageView: ImageView, url: String?, radius: Float) {
    url?.let {
      Glide.with(imageView).load(it).apply(
        RequestOptions.bitmapTransform(GaussianBlurTransformation(imageView.context, radius))
      ).into(
        imageView
      )
    }
  }

  @JvmStatic
  @BindingAdapter("loadAdapter")
  fun loadAdapter(recyclerView: RecyclerView, mu: MutableLiveData<ListData>) {
    val adapter = recyclerView.adapter
    if (adapter is RecyclerViewAdapter) {
      adapter.refreshData(mu.value?.header, mu.value?.item)
    }
  }


  @JvmStatic
  @BindingAdapter("loadAdapter")
  fun loadAdapter(recyclerView: RecyclerView, data: ObservableField<List<ListItem>>) {
    val adapter = recyclerView.adapter
    if (adapter is CustomAdapter) {
      adapter.refreshData(data.get())
    } else if (adapter is TransitionAdapter) {
      adapter.refreshData(data.get())
    }
  }

  @JvmStatic
  @BindingAdapter("loadHeaderAdapter")
  fun loadHeaderAdapter(recyclerView: RecyclerView, list: List<String>?) {
    val adapter = recyclerView.adapter
    if (adapter is HeaderViewAdapter) {
      adapter.refreshData(list)
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
