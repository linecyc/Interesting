package com.linecy.interesting.ui.gaussianblur

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.linecy.interesting.R
import com.linecy.interesting.navigation.Navigator
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.utils.BindUIUtil
import kotlinx.android.synthetic.main.activity_gaussian_blur.imageView
import kotlinx.android.synthetic.main.activity_gaussian_blur.ivBlur
import kotlinx.android.synthetic.main.activity_gaussian_blur.seekBar
import kotlinx.android.synthetic.main.activity_gaussian_blur.tvBlur

/**
 * @author by linecy.
 */
class GaussianBlurActivity : BaseActivity<ViewDataBinding>() {

  private lateinit var url: String

  override fun layoutResId(): Int {
    return R.layout.activity_gaussian_blur
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    url = intent.getStringExtra(Navigator.EXTRA_URL)
    seekBar.max = 25
    seekBar.setOnSeekBarChangeListener(SeekBarChange())
    BindUIUtil.loadUrl(imageView, url)
    seekBar.progress = 10
  }

  inner class SeekBarChange : OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
      tvBlur.text = ("高斯模糊半径为：$progress")
      if (progress > 0) {
        BindUIUtil.setBlurPhoto(ivBlur, url, progress.toFloat())
      }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

  }

}