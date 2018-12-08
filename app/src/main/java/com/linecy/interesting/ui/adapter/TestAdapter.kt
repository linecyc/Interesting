package com.linecy.interesting.ui.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.databinding.LayoutItemBinding
import com.linecy.interesting.ui.adapter.TestAdapter.ItemViewHolder

/**
 * @author by linecy.
 */
class TestAdapter(private val activity: Activity) : RecyclerView.Adapter<ItemViewHolder>() {

  private val list = ArrayList<ListItem>()//列表数据
  private val inflater = LayoutInflater.from(activity)

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
    val binding = DataBindingUtil.inflate<LayoutItemBinding>(
      inflater, R.layout.layout_item, p0,
      false
    )
    return ItemViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun onBindViewHolder(p0: ItemViewHolder, p1: Int) {
    p0.bindData(list[p1], p1)
  }

  fun refreshData(list: List<ListItem>?) {
    this.list.clear()
    if (list?.size ?: 0 > 0) {
      this.list.addAll(list!!)
    }
    notifyDataSetChanged()
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
//      when (index) {
//        0 -> Navigator.navigateToGaussianBlur(activity, binding.root.ivLogo, url)
//        else -> Navigator.navigateToTransitionWithAnim(activity, binding.root.ivLogo, url)
//      }
    }

    fun bindData(item: ListItem, position: Int) {
      this.url = item.url
      this.index = position
      binding.setVariable(BR.item, item)
      binding.executePendingBindings()
    }
  }
}