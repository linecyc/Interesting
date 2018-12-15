package com.linecy.interesting.ui.home.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.data.Response
import com.linecy.interesting.ui.adapter.CustomAdapter
import kotlinx.android.synthetic.main.fragment_search.etSearch
import kotlinx.android.synthetic.main.fragment_search.ivClose
import kotlinx.android.synthetic.main.fragment_search.recyclerView


/**
 * 自定义布局的搜索页面。
 *
 * @author by linecy.
 */

class SearchFragment @SuppressLint("ValidFragment")
private constructor() : BottomSheetDialogFragment(), OnClickListener {

  companion object {

    @JvmStatic
    fun getInstance(): SearchFragment {
      val fragment = SearchFragment()
      val bundle = Bundle()
      fragment.arguments = bundle
      return fragment
    }

    @JvmStatic
    fun show(manager: FragmentManager, tag: String?, view: View) {
      val ft = manager.beginTransaction()
      val fragment = SearchFragment()
      val bundle = Bundle()
      fragment.arguments = bundle
      ft.addToBackStack(null)
      ft.addSharedElement(view, view.transitionName)
      fragment.show(ft, tag)
    }
  }

  private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

  override fun onStart() {
    super.onStart()
    dialog?.let {
      val bottomSheet = it.findViewById<View>(R.id.design_bottom_sheet)
      bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
      it.setCancelable(false)//会设置behavior.isHideable = false，不会导致向下拖动时隐藏
      it.setCanceledOnTouchOutside(false)
    }
    view?.run {
      post {
        val parent = this.parent as View
        val params = parent.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        bottomSheetBehavior = behavior as BottomSheetBehavior<View>
        bottomSheetBehavior?.run {
          //skipCollapsed = false
          //isHideable = false
          setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {
              when (p1) {
                BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                //回弹效果
                else -> bottomSheetBehavior?.state =
                    BottomSheetBehavior.STATE_EXPANDED
              }
            }

          })
          //全屏
          state = BottomSheetBehavior.STATE_EXPANDED
        }
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_search, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    ivClose.setOnClickListener(this)
    etSearch.setOnClickListener(this)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    dialog?.setOnDismissListener {
      hideSystemBottomNavigation()
    }
    context?.let {
      val searchAdapter =
        CustomAdapter(it, intArrayOf(R.layout.item_search), intArrayOf(BR.searchItem))
      val manager = GridLayoutManager(it, 6)
      manager.spanSizeLookup = object : SpanSizeLookup() {
        override fun getSpanSize(p0: Int): Int {
          return when (p0 % 2 == 0) {
            true -> 3
            false -> 2
          }
        }
      }
      recyclerView.run {
        adapter = searchAdapter
        layoutManager = manager
      }
      searchAdapter.refreshData(Response.createStringList(10))
    }
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.ivClose -> {
        dismiss()
      }
    }
  }

  private fun hideSystemBottomNavigation() {
    activity?.window?.decorView?.let {
      it.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
  }
}