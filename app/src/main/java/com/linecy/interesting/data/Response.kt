package com.linecy.interesting.data

import com.linecy.interesting.utils.Constants

/**
 * @author by linecy.
 */

object Response {

  fun createListData(size: Int): List<ListItem> {
    val list = ArrayList<ListItem>(size)
    for (i in 0 until size) {
      list.add(
        ListItem(
          url = Constants.PHOTO2_URL,
          name = "第${i + 1}个你敢信？？", detail = "这是第${i + 1}个的描述，一定要长长长长长长长长长长长长长长长~~"
        )
      )
    }
    return list
  }

  fun createHeaderData(): List<String> {
    return listOf(
      Constants.PHOTO1_URL,
      Constants.PHOTO3_URL,
      Constants.PHOTO4_URL,
      Constants.PHOTO5_URL,
      Constants.PHOTO6_URL
    )
  }


  fun createTransitionData(size: Int): List<ListItem> {
    val list = ArrayList<ListItem>()

    list.add(
      ListItem(
        url = Constants.PHOTO3_URL,
        name = Constants.SCENE,
        detail = "视图各种属性的集合，被配置后可以自动运行，同时为场景间切换提供过度的动画，当然，还可以自定义"
      )
    )

    list.add(
      ListItem(
        url = Constants.PHOTO5_URL,
        name = Constants.SLIDE_TRANSITION,
        detail = "将视图从边缘（上、下、左、右）移入和移出，可以设置插值器来改变速率,通过代码和创建transition动画来实现"
      )
    )
    list.add(
      ListItem(
        url = Constants.PHOTO6_URL,
        name = Constants.EXPLODE_TRANSITION,
        detail = "同样是将视图从边缘移入和移出，不同于SLIDE的是他是“爆炸”式的效果，是由内到外的"
      )
    )

    list.add(
      ListItem(
        url = Constants.PHOTO7_URL,
        name = Constants.FADE_TRANSITION,
        detail = "这是一个将视图层级渐变的效果，淡入淡出"
      )
    )

    list.add(
      ListItem(
        url = Constants.PHOTO4_URL,
        name = Constants.SHARED_ELEMENT,
        detail = "两个页面元素的共享，实现一些特别的效果，同时可以配合其他属性，来更改元素的属性和动画"
      )
    )

    return list
  }
}