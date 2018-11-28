package com.linecy.interesting.ui.home

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.widget.LinearLayoutManager
import com.linecy.interesting.BR
import com.linecy.interesting.R
import com.linecy.interesting.R.color
import com.linecy.interesting.R.layout
import com.linecy.interesting.databinding.FragmentListBinding
import com.linecy.interesting.ui.BaseFragment
import com.linecy.interesting.ui.home.adapter.ListAdapter
import com.linecy.interesting.ui.home.adapter.StickyHeaderDecoration
import com.linecy.interesting.ui.misc.ViewContainer
import com.linecy.interesting.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.swipeLayout
import kotlinx.android.synthetic.main.fragment_list.viewContainer

/**
 * @author by linecy.
 */

class ListFragment : BaseFragment<FragmentListBinding>(), OnRefreshListener, ViewContainer.EmptyCallback, ViewContainer.ErrorCallback {

  private lateinit var viewModel: ListViewModel
  private lateinit var layoutManager: LinearLayoutManager

  override fun layoutResId(): Int {
    return R.layout.fragment_list
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    swipeLayout.setOnRefreshListener(this)
    swipeLayout.setColorSchemeResources(color.colorPrimary)
    viewContainer.setEmptyCallback(this)
    viewContainer.setErrorCallback(this)
    viewContainer.setDisplayedChildId(R.id.swipeLayout)
    viewModel = viewModelProvider.get(ListViewModel::class.java)
    fragmentBinding?.let {
      it.setVariable(BR.listViewModel, viewModel)
      context?.run {
        val adapter = ListAdapter(this)
        it.recyclerView.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        it.recyclerView.layoutManager = layoutManager
        it.recyclerView.addItemDecoration(
            StickyHeaderDecoration(layoutInflater.inflate(layout.layout_stick_header, null)))
        onRefresh()
      }
    }
  }

  override fun onRefresh() {
    viewModel.refreshData()
  }

  override fun onShowEmpty() {
    onRefresh()
  }

  override fun onShowError() {
    onRefresh()
  }

}