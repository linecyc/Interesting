package com.linecy.interesting.ui

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.linecy.interesting.viewmodel.ViewModelFactory

/**
 * @author by linecy.
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

  protected var fragmentBinding: VB? = null
  protected lateinit var viewModelProvider: ViewModelProvider

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    if (layoutResId() != 0) {
      fragmentBinding = DataBindingUtil.inflate(inflater, layoutResId(), container,
          false)
      return fragmentBinding?.root ?: inflater.inflate(layoutResId(), container, false)
    }
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.application?.let {
      viewModelProvider = ViewModelProvider(this, ViewModelFactory.getInstance(it))
    }
    fragmentBinding?.setLifecycleOwner(this)
    onInitView(savedInstanceState)
  }

  abstract fun layoutResId(): Int

  abstract fun onInitView(savedInstanceState: Bundle?)
}