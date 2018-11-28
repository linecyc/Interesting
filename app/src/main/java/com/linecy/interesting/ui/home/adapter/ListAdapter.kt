package com.linecy.interesting.ui.home.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.databinding.LayoutHeaderBinding
import com.linecy.interesting.databinding.LayoutItemBinding

/**
 * @author by linecy.
 */
class ListAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {

  private var headerUrl = ""
  private val list = ArrayList<ListItem>()//列表数据

  private val typeHeader = 1//头
  private val typeItem = 2//列表
  private val inflater = LayoutInflater.from(context)

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
    return when (p1) {
      typeHeader -> {
        val binding = DataBindingUtil.inflate<LayoutHeaderBinding>(inflater, R.layout.layout_header,
            p0, false)
        HeaderViewHolder(binding)
      }
      else -> {
        val binding = DataBindingUtil.inflate<LayoutItemBinding>(inflater, R.layout.layout_item, p0,
            false)
        ItemViewHolder(binding)
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when (position) {
      0 -> typeHeader
      else -> typeItem
    }
  }

  override fun getItemCount(): Int {
    return list.size + 1
  }

  override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
    when (getItemViewType(p1)) {
      typeHeader -> (p0 as HeaderViewHolder).bindData(headerUrl)
      else -> (p0 as ItemViewHolder).bindData(list[p1 - 1])
    }
  }


  fun refreshData(url: String?, list: List<ListItem>?) {
    url?.let {
      this.headerUrl = it
    }
    if (list?.size ?: 0 > 0) {
      this.list.addAll(list!!)
    }
    notifyDataSetChanged()
  }

  class HeaderViewHolder(private val binding: LayoutHeaderBinding) : ViewHolder(binding.root) {
    fun bindData(url: String?) {
      binding.setVariable(BR.url, url)
      binding.executePendingBindings()
    }
  }

  inner class ItemViewHolder(private val binding: LayoutItemBinding) : ViewHolder(binding.root) {

    fun bindData(item: ListItem) {
      binding.setVariable(BR.item, item)
      binding.executePendingBindings()
    }


  }
}