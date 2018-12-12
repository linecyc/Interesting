package com.linecy.interesting.ui.home.adapter

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
import com.linecy.interesting.databinding.ItemTransitionBinding
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.ui.home.adapter.TransitionAdapter.ItemViewHolder
import com.linecy.interesting.utils.AnimatorUtils
import kotlinx.android.synthetic.main.item_transition.view.ivBackFlip
import kotlinx.android.synthetic.main.item_transition.view.ivFrontFlip
import kotlinx.android.synthetic.main.item_transition.view.layoutBack
import kotlinx.android.synthetic.main.item_transition.view.layoutFront

/**
 *
 * 因为rotationX/Y动画后，导致对应item的front变成GONE了，同时因为dataBinding的缘故，在以后的回调中
 * 没有调用bindData()，而是调用对应的@BindingAdapter方法来回填数据，这样的话导致重置页面的动作只能放
 * 在@BindingAdapter方法里面来做
 *
 *
 * @author by linecy.
 */
class TransitionAdapter(private val activity: Activity) : RecyclerView.Adapter<ItemViewHolder>() {

  private val list = ArrayList<ListItem>()//列表数据
  private val inflater = LayoutInflater.from(activity)

  override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
    val binding = DataBindingUtil.inflate<ItemTransitionBinding>(
      inflater, R.layout.item_transition, p0,
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

  inner class ItemViewHolder(private val binding: ItemTransitionBinding) : ViewHolder(
    binding.root
  ), OnClickListener {
    private var url: String? = null
    private var index: Int = 0

    init {
      binding.root.setOnClickListener(this)
      binding.root.ivFrontFlip.setOnClickListener(this)
      binding.root.ivBackFlip.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
      when (p0?.id) {
        R.id.ivFrontFlip, R.id.ivBackFlip -> {
          if (index % 2 == 0) {
            AnimatorUtils.rotationX(binding.root.layoutFront, binding.root.layoutBack)
          } else {
            AnimatorUtils.rotationY(binding.root.layoutFront, binding.root.layoutBack)
          }
        }
        else -> {
          when (index) {
            0 -> Navigator.navigateToScene(activity)
            1 -> Navigator.navigateToSlide(activity)
            2 -> Navigator.navigateToExplode(activity)
            3 -> Navigator.navigateToFade(activity)
            4 -> {
              if (View.VISIBLE == binding.layoutFront.visibility) {
                Navigator.navigateToSharedElement(activity, binding.ivFront, url)
              } else {
                Navigator.navigateToSharedElement(activity, binding.ivBack, url)
              }
            }
            5 -> {
              if (View.VISIBLE == binding.layoutFront.visibility) {
                Navigator.navigateToCircularReveal(activity, binding.ivFront, url)
              } else {
                Navigator.navigateToCircularReveal(activity, binding.ivBack, url)
              }
            }
          }
        }
      }
    }

    /**
     * dataBinding后不会走这个方法,而是调用对应的@BindingAdapter对应的方法
     */
    fun bindData(item: ListItem, position: Int) {
      this.url = item.url
      this.index = position
      binding.setVariable(BR.itemTransition, item)
      binding.executePendingBindings()
    }
  }
}