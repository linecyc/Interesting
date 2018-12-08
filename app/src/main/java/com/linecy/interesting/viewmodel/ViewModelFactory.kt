package com.linecy.interesting.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting

/**
 * @author by linecy.
 */
class ViewModelFactory(application: Application) : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T = (when {
    modelClass.isAssignableFrom(RecyclerViewViewModel::class.java) -> RecyclerViewViewModel()
    modelClass.isAssignableFrom(TransitionViewModel::class.java) -> TransitionViewModel()
    else -> throw IllegalArgumentException("unknown model class $modelClass")
  }) as T

  companion object {

    @SuppressLint("StaticFieldLeak")
    @Volatile
    private var INSTANCE: ViewModelFactory? = null

    fun getInstance(application: Application) = INSTANCE ?: synchronized(
      ViewModelFactory::class.java
    ) {
      INSTANCE ?: ViewModelFactory(application)
        .also { INSTANCE = it }
    }

    @VisibleForTesting
    fun destroyInstance() {
      INSTANCE = null
    }
  }
}