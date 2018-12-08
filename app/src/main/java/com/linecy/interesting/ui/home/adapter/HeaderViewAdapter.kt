package com.linecy.interesting.ui.home.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.databinding.ItemHeaderBinding
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.ui.home.adapter.HeaderViewAdapter.HeaderViewHolder
import kotlinx.android.synthetic.main.item_header.view.imageView

/**
 * @author by linecy.
 */
class HeaderViewAdapter(private val activity: Activity) : Adapter<HeaderViewHolder>() {

  private val list = ArrayList<String>()

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HeaderViewHolder {
    return HeaderViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(p0.context),
        R.layout.item_header,
        p0,
        false
      )
    )
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun onBindViewHolder(p0: HeaderViewHolder, p1: Int) {
    p0.bindData(list[p1])
  }

  fun refreshData(list: List<String>?) {
    this.list.clear()
    if (list != null && list.isNotEmpty()) {
      this.list.addAll(list)
    }
    notifyDataSetChanged()
  }

  inner class HeaderViewHolder(private val binding: ItemHeaderBinding) :
    ViewHolder(binding.root), OnClickListener {
    private var url: String? = null

    init {
      binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
      Navigator.navigateToGaussianBlur(activity, binding.root.imageView, url)
    }

    fun bindData(url: String) {
      this.url = url
      binding.setVariable(BR.headerUrl, url)
      binding.executePendingBindings()
    }
  }
}