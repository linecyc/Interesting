package com.linecy.interesting

import android.app.Application
import timber.log.Timber

/**
 * @author by linecy.
 */
class InterestingApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

}