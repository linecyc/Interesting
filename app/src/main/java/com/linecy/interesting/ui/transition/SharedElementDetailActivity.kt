package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import com.linecy.interesting.R
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.utils.BindUIUtil
import kotlinx.android.synthetic.main.activity_shared_element_detail.imageView
import kotlinx.android.synthetic.main.activity_shared_element_detail.tvName

/**
 * @author by linecy.
 */
class SharedElementDetailActivity : BaseActivity<ViewDataBinding>() {

  override fun layoutResId(): Int {
    return R.layout.activity_shared_element_detail
  }

  override fun onInitView(savedInstanceState: Bundle?) {

    val listItem = intent.getParcelableExtra<ListItem>(Navigator.EXTRA_DATA)
    if (listItem != null) {
      BindUIUtil.loadUrl(imageView, listItem.url)
      tvName.text = listItem.name
    }
  }
}