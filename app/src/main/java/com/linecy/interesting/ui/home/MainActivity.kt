package com.linecy.interesting.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.linecy.interesting.R.id
import com.linecy.interesting.R.layout

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    supportFragmentManager.beginTransaction().replace(id.fragmentLayout,
        ListFragment(),
        ListFragment::class.java.simpleName).commitNow()
  }
}
