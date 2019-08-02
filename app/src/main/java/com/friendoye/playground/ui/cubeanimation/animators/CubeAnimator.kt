package com.friendoye.playground.ui.cubeanimation.animators

import android.view.View


interface CubeAnimator {
    fun View.startExpandAnimation(immediate: Boolean)
    fun View.startCollapseAnimation(immediate: Boolean)
}