package com.linecy.interesting.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author by linecy.
 */
abstract class BaseActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }


  abstract fun layoutResId(): Int

  abstract fun onInitView(savedInstanceState: Bundle?)

}