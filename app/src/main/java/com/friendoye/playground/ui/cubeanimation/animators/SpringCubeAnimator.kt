package com.friendoye.playground.ui.cubeanimation.animators

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_NO_BOUNCY
import com.friendoye.playground.R
import androidx.dynamicanimation.animation.SpringForce.STIFFNESS_VERY_LOW as STIFFNESS_VERY_LOW1


class SpringCubeAnimator(private val context: Context) : CubeAnimator {
    private val startColor by lazy { ContextCompat.getColor(context, R.color.startColor) }
    private val endColor by lazy { ContextCompat.getColor(context, R.color.endColor) }

    private val runningAnimatorsSet = HashSet<SpringAnimation>()

    override fun View.startExpandAnimation(immediate: Boolean) {
        if (scaleX == 4.0f && scaleY == 4.0f && backgroundColor == endColor) {
            return
        }

        runningAnimatorsSet += spring(SpringAnimation.SCALE_X).apply {
            animateToFinalPosition(4.0f)
        }
        runningAnimatorsSet += spring(SpringAnimation.SCALE_Y).apply {
            animateToFinalPosition(4.0f)
        }
        runningAnimatorsSet += backgroundColorSpring(startColor, endColor).apply {
            animateToFinalPosition(1.0f)
        }

        if (immediate) {
            runningAnimatorsSet.forEach { it.skipToEnd() }
        }
    }

    override fun View.startCollapseAnimation(immediate: Boolean) {
        if (scaleX == 1.0f && scaleY == 1.0f && backgroundColor == startColor) {
            return
        }

        runningAnimatorsSet += spring(SpringAnimation.SCALE_X).apply {
            animateToFinalPosition(1.0f)
        }
        runningAnimatorsSet += spring(SpringAnimation.SCALE_Y).apply {
            animateToFinalPosition(1.0f)
        }
        runningAnimatorsSet += backgroundColorSpring(startColor, endColor).apply {
            animateToFinalPosition(0.0f)
        }

        if (immediate) {
            runningAnimatorsSet.forEach { it.skipToEnd() }
        }
    }
}