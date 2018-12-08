package com.linecy.interesting.navigation

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.View
import com.linecy.interesting.ui.gaussianblur.GaussianBlurActivity
import com.linecy.interesting.ui.transition.BottomSheetActivity
import com.linecy.interesting.ui.transition.ExplodeActivity
import com.linecy.interesting.ui.transition.FadeActivity
import com.linecy.interesting.ui.transition.SceneActivity
import com.linecy.interesting.ui.transition.SharedElementActivity
import com.linecy.interesting.ui.transition.SlideActivity
import com.linecy.interesting.ui.transition.TransitionActivity

/**
 * @author by linecy.
 */


object Navigator {

  const val EXTRA_URL = "extra_url"


  fun navigateToGaussianBlur(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(activity, GaussianBlurActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          activity,
          view, view.transitionName
        ).toBundle()
      )
    }
  }


  fun navigateToBottomSheet(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(activity, BottomSheetActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          activity,
          view, view.transitionName
        ).toBundle()
      )
    }
  }


  fun navigateToScene(activity: Activity?) {
    activity?.run {
      startActivity(Intent(activity, SceneActivity::class.java))
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
        val intent = Intent(activity, SlideActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
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
        val intent = Intent(activity, ExplodeActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
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
        val intent = Intent(activity, FadeActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
      }
    }
  }

  fun navigateToSharedElement(activity: Activity?, view: View, url: String?) {
    activity?.run {
      val intent = Intent(activity, SharedElementActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          activity,
          view, view.transitionName
        ).toBundle()
      )
    }
  }

  fun navigateToTransitionWithAnim(activity: Activity?, view: View, url: String?) {
    activity?.run {
      window?.let {
        //it.enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.explode)
        //it.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.explode)

//        it.enterTransition = Slide().setDuration(1000)
//        it.exitTransition = Slide().setDuration(1000)
      }
      val intent = Intent(activity, TransitionActivity::class.java)
      intent.putExtra(EXTRA_URL, url)
      startActivity(
        intent, ActivityOptions.makeSceneTransitionAnimation(
          activity,
          view, view.transitionName
        ).toBundle()
      )
    }
  }


}