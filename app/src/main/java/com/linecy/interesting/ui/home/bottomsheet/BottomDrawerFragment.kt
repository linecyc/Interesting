package com.linecy.interesting.ui.home.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.NavigationView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.linecy.interesting.R

/**
 * 通过navigationView填充menu的形式添加布局
 * 也可以自定义布局
 *
 * @author by linecy.
 */

private const val EXTRA_MENU_ID = "extra_menu_id"

class BottomDrawerFragment @SuppressLint("ValidFragment")
private constructor() : BottomSheetDialogFragment() {

  private var listener: BottomDrawerItemClickListener? = null


  companion object {

    @JvmStatic
    fun getInstance(@MenuRes menuLayoutIdRes: Int): BottomDrawerFragment {
      val fragment = BottomDrawerFragment()
      val bundle = Bundle()
      bundle.putInt(EXTRA_MENU_ID, menuLayoutIdRes)
      fragment.arguments = bundle
      return fragment
    }
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    arguments?.getInt(EXTRA_MENU_ID, 0)?.let {
      return if (it != 0) {
        val view = inflater.inflate(R.layout.layout_bottom_navigation_menu, container, false)
        val navigationView = view.findViewById<NavigationView>(R.id.navigationView)
        navigationView.inflateMenu(it)
        view
      } else {
        super.onCreateView(inflater, container, savedInstanceState)
      }
    } ?: return super.onCreateView(inflater, container, savedInstanceState)
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val navigationView = view.findViewById<NavigationView>(R.id.navigationView)
    navigationView.setNavigationItemSelectedListener { menuItem ->
      //每次点击后重置选项
      menuItem.isChecked = !menuItem.isChecked
      listener?.let {
        it.onBottomDrawerItemClick(menuItem)
        dismiss()
        return@setNavigationItemSelectedListener true
      } ?: return@setNavigationItemSelectedListener false
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    dialog?.setOnDismissListener {
      hideSystemBottomNavigation()
    }
  }

  private fun hideSystemBottomNavigation() {
    activity?.window?.decorView?.let {
      it.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
  }


  fun setBottomDrawerItemClickListener(listener: BottomDrawerItemClickListener) {
    this.listener = listener
  }

  interface BottomDrawerItemClickListener {

    fun onBottomDrawerItemClick(item: MenuItem)
  }
}