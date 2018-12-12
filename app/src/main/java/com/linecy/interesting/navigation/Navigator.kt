package com.linecy.interesting.navigation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.util.Pair
import android.view.Gravity
import android.view.View
import com.linecy.interesting.data.ListItem
import com.linecy.interesting.ui.gaussianblur.GaussianBlurActivity
import com.linecy.interesting.ui.transition.BottomSheetActivity
import com.linecy.interesting.ui.transition.CircularRevealActivity
import com.linecy.interesting.ui.transition.ExplodeActivity
import com.linecy.interesting.ui.transition.FadeActivity
import com.linecy.interesting.ui.transition.SceneActivity
import com.linecy.interesting.ui.transition.SharedElementActivity
import com.linecy.interesting.ui.transition.SharedElementDetailActivity
import com.linecy.interesting.ui.transition.SlideActivity

/**
 * @author by linecy.
 */


object Navigator {

  const val EXTRA_URL = "extra_url"
  const val EXTRA_DATA = "extra_data"


  fun navigateToGaussianBlur(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(this, GaussianBlurActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          this,
          view, view.transitionName
        ).toBundle()
      )
    }
  }


  fun navigateToBottomSheet(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(this, BottomSheetActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          this,
          view, view.transitionName
        ).toBundle()
      )
    }
  }


  fun navigateToScene(activity: Activity?) {
    activity?.run {
      startActivity(Intent(this, SceneActivity::class.java))
      //只在这调用的话，退出时没有动画，需要在finish()方法里面添加
      //overridePendingTransition(R.anim.slide_bottom_in, R.anim.alpha_stable)
    }
  }

  fun navigateToSlide(activity: Activity?) {
    activity?.run {
      window?.let {
        val slide = Slide()
        slide.slideEdge = Gravity.START
        slide.duration = 500
        it.enterTransition = slide
        it.exitTransition = slide
        it.reenterTransition = slide
        it.returnTransition = slide
        val intent = Intent(this, SlideActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
      }
    }
  }

  fun navigateToExplode(activity: Activity?) {
    activity?.run {
      window?.let {
        val explode = Explode()
        explode.duration = 500
        it.enterTransition = explode
        it.exitTransition = explode
        it.reenterTransition = explode
        it.returnTransition = explode
        val intent = Intent(this, ExplodeActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
      }
    }
  }

  fun navigateToFade(activity: Activity?) {
    activity?.run {
      window?.let {
        val fade = Fade()
        fade.duration = 500
        it.enterTransition = fade
        it.exitTransition = fade
        it.reenterTransition = fade
        it.returnTransition = fade
        val intent = Intent(this, FadeActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
      }
    }
  }

  fun navigateToSharedElement(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(this, SharedElementActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          this,
          view, view.transitionName
        ).toBundle()
      )
    }
  }

  fun navigateToSharedElementDetail(
    activity: Activity?,
    listItem: ListItem,
    vararg pair: Pair<View, String>
  ) {
    activity?.run {
      val intent = Intent(this, SharedElementDetailActivity::class.java)
      intent.putExtra(EXTRA_DATA, listItem)
      startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, *pair).toBundle())
    }
  }


  fun navigateToCircularReveal(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(this, CircularRevealActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent,
        ActivityOptions.makeSceneTransitionAnimation(this, view, view.transitionName).toBundle()
      )
    }
  }
}