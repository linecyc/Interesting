package com.linecy.interesting.ui.home.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.databinding.LayoutHeaderBinding
import com.linecy.interesting.databinding.LayoutItemBinding
import com.linecy.interesting.navigation.Navigator
import kotlinx.android.synthetic.main.layout_item.view.ivLogo

/**
 * @author by linecy.
 */
class RecyclerViewAdapter(private val activity: Activity) : RecyclerView.Adapter<ViewHolder>() {

  private var headerUrl = ArrayList<String>()
  private val list = ArrayList<ListItem>()//列表数据

  private val typeHeader = 1//头
  private val typeItem = 2//列表
  private val inflater = LayoutInflater.from(activity)

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
    return when (p1) {
      typeHeader -> {
        val binding = DataBindingUtil.inflate<LayoutHeaderBinding>(
          inflater, R.layout.layout_header,
          p0, false
        )
        HeaderViewHolder(binding)
      }
      else -> {
        val binding = DataBindingUtil.inflate<LayoutItemBinding>(
          inflater, R.layout.layout_item, p0,
          false
        )
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
      else -> (p0 as ItemViewHolder).bindData(list[p1 - 1], p1 - 1)
    }
  }

  fun refreshData(url: List<String>?, list: List<ListItem>?) {
    this.headerUrl.clear()
    this.list.clear()
    if (url?.size ?: 0 > 0) {
      this.headerUrl.addAll(url!!)
    }
    if (list?.size ?: 0 > 0) {
      this.list.addAll(list!!)
    }
    notifyDataSetChanged()
  }

  fun onItemRemoved(position: Int) {
    list.removeAt(position - 1)
    notifyItemRemoved(position)
  }

  inner class HeaderViewHolder(private val binding: LayoutHeaderBinding) :
    ViewHolder(binding.root) {

    init {
      binding.recyclerView.run {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = HeaderViewAdapter(activity)
        PagerSnapHelper().attachToRecyclerView(this)
      }
    }

    fun bindData(url: List<String>) {
      binding.setVariable(BR.headerList, url)
      binding.executePendingBindings()
    }
  }

  inner class ItemViewHolder(private val binding: LayoutItemBinding) : ViewHolder(
    binding.root
  ), OnClickListener {
    private var url: String? = null
    private var index: Int = 0

    init {
      binding.root.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      Navigator.navigateToBottomSheet(activity, binding.root.ivLogo, url)
    }

    fun bindData(item: ListItem, position: Int) {
      this.url = item.url
      this.index = position
      binding.setVariable(BR.item, item)
      binding.executePendingBindings()
    }


  }
}