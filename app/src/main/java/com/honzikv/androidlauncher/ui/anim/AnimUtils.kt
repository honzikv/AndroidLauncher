package com.honzikv.androidlauncher.ui.anim

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

/**
 * Performs an animation from [animationResourceId] on [recyclerView]
 */
fun runAnimationOnRecyclerView(recyclerView: RecyclerView, animationResourceId: Int) {
    val context = recyclerView.context
    val controller = AnimationUtils.loadLayoutAnimation(context, animationResourceId)

    recyclerView.layoutAnimation = controller
    recyclerView.adapter?.notifyDataSetChanged()
    recyclerView.scheduleLayoutAnimation()
}