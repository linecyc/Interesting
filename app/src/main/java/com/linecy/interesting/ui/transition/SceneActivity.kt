package com.linecy.interesting.ui.transition

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.MenuItem
import android.widget.ImageView
import com.linecy.interesting.R
import com.linecy.interesting.ui.BaseActivity
import com.linecy.interesting.ui.home.bottomsheet.BottomDrawerFragment
import com.linecy.interesting.utils.DisplayUtils
import kotlinx.android.synthetic.main.activity_scene.rootScene

/**
 * 1、切换的Scene需要有相同的id，比如这里的layoutScene。
 * 2、视图各种属性的集合，被配置后可以自动运行，同时为场景间切换提供过度的动画，当然，还可以自定义。
 *
 * @author by linecy.
 */
class SceneActivity : BaseActivity<ViewDataBinding>(),
  BottomDrawerFragment.BottomDrawerItemClickListener {

  private lateinit var scene1: Scene
  private lateinit var scene2: Scene
  private lateinit var scene3: Scene
  private lateinit var scene4: Scene
  private lateinit var scene5: Scene
  private lateinit var transitionManager: TransitionManager
  private var currentScene: Int = 1

  override fun layoutResId(): Int {
    return R.layout.activity_scene
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    //进入之前才有效？，亲测在super.onCreate(savedInstanceState)之后执行同样有效
    //进入时的退出动画一定要设置，不然会黑屏
//    overridePendingTransition(R.anim.slide_bottom_in, R.anim.alpha_stable)
    super.onCreate(savedInstanceState)
    overridePendingTransition(R.anim.slide_bottom_in, R.anim.alpha_stable)
  }

  override fun finish() {
    //退出之后才有效
    //退出时的进入动画可以不设置
    super.finish()
    overridePendingTransition(0, R.anim.slide_bottom_out)
  }

  override fun onInitView(savedInstanceState: Bundle?) {
    setBottomFabImageResource(R.drawable.ic_refresh_white_24dp)

    //从已有的视图层中获取scene
    scene1 = Scene(rootScene, rootScene.findViewById(R.id.layoutScene))

    //从自定义布局中加载scene
    scene2 = Scene.getSceneForLayout(rootScene, R.layout.layout_scene2, this)
    scene3 = Scene.getSceneForLayout(rootScene, R.layout.layout_scene3, this)
    scene4 = Scene.getSceneForLayout(rootScene, R.layout.layout_scene4, this)
    scene5 = Scene.getSceneForLayout(rootScene, R.layout.layout_scene5, this)

    //创建一个自定义TransitionManager
    transitionManager = TransitionInflater.from(this)
      .inflateTransitionManager(R.transition.scene_transition_manager, rootScene)
  }


  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.navigation_search -> {
        true
      }
      R.id.navigation_menu
      -> {
        val bottomDrawerFragment = BottomDrawerFragment.getInstance(R.menu.bottom_scene_menu)
        bottomDrawerFragment.setBottomDrawerItemClickListener(this)
        bottomDrawerFragment.show(supportFragmentManager, SceneActivity::class.java.simpleName)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  /**
   * 如果直接从Scene2到Scene4的话，是直接平移，而不是渐变2->3->4
   *
   */
  override fun onBottomAppBarFab() {

    when (currentScene) {
      1 -> {
        TransitionManager.go(scene2)
        currentScene = 2
      }
      2 -> {
        TransitionManager.go(scene3)
        currentScene = 3
      }
      3 -> {
        TransitionManager.go(scene4)
        currentScene = 4
      }
      4 -> {
        //TransitionManager.go(scene5)
        transitionManager.transitionTo(scene5)
        currentScene = 5
      }
      5 -> {
        //改变布局，也可以通过transitionManager.transitionTo()和TransitionManager.go()
        TransitionManager.beginDelayedTransition(rootScene)
        val ivRed = rootScene.findViewById<ImageView>(R.id.ivRed)
        val ivGreen = rootScene.findViewById<ImageView>(R.id.ivGreen)
        val params = ivRed.layoutParams
        val params2 = ivGreen.layoutParams
        val newSize = DisplayUtils.dp2px(this, 150f).toInt()

        params.width = newSize
        params.height = newSize
        ivRed.layoutParams = params

        params2.width = newSize
        params2.height = newSize
        ivGreen.layoutParams = params2
        currentScene = 6
      }
      else -> {
        TransitionManager.go(
          scene1,
          TransitionInflater.from(this).inflateTransition(R.transition.change_bounds_fade_in_together)
        )
        currentScene = 1
      }
    }
  }


  override fun onBottomDrawerItemClick(item: MenuItem) {
    val id = item.itemId
    when {
      id == R.id.scene1 && item.isChecked -> {
        TransitionManager.go(scene1)
        currentScene = 1
      }
      id == R.id.scene2 && item.isChecked -> {
        TransitionManager.go(scene2)
        currentScene = 2
      }
      id == R.id.scene3 && item.isChecked -> {
        TransitionManager.go(scene3)
        currentScene = 3
      }
      id == R.id.scene4 && item.isChecked -> {
        TransitionManager.go(scene4)
        currentScene = 4
      }
      id == R.id.scene5 && item.isChecked -> {
        transitionManager.transitionTo(scene5)
        currentScene = 5
      }
      id == R.id.scene6 && item.isChecked -> {
        TransitionManager.beginDelayedTransition(rootScene)
        val ivRed = rootScene.findViewById<ImageView>(R.id.ivRed)
        val ivGreen = rootScene.findViewById<ImageView>(R.id.ivGreen)
        val params = ivRed.layoutParams
        val params2 = ivGreen.layoutParams
        val newSize = DisplayUtils.dp2px(this, 150f).toInt()

        params.width = newSize
        params.height = newSize
        ivRed.layoutParams = params

        params2.width = newSize
        params2.height = newSize
        ivGreen.layoutParams = params2
        currentScene = 6
      }
    }
  }
}