package com.friendoye.playground.ui.cubeanimation.animators

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.friendoye.playground.R

class CubeObjectAnimator(private val context: Context) : CubeAnimator {
    private val startColor by lazy { ContextCompat.getColor(context, R.color.startColor) }
    private val endColor by lazy { ContextCompat.getColor(context, R.color.endColor) }
    private val duration = 3000L

    private var objectAnimator: ObjectAnimator? = null

    override fun View.startExpandAnimation(immediate: Boolean) {
        if (objectAnimator?.isRunning == true && !immediate) {
            objectAnimator?.reverse()
            return
        }

        objectAnimator?.end()

        val duration = if (immediate) 0L else duration
        val scaleXPropHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 4f)
        val scaleYPropHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 4f)
        val bgPropHolder = PropertyValuesHolder.ofObject(BackgroundColorProperty(),
            ArgbEvaluator(), startColor, endColor)

        objectAnimator = ObjectAnimator
            .ofPropertyValuesHolder(this,
                scaleXPropHolder, scaleYPropHolder, bgPropHolder
            )
            .setDuration(duration)

        objectAnimator?.start()
    }

    override fun View.startCollapseAnimation(immediate: Boolean) {
        if (objectAnimator?.isRunning == true && !immediate) {
            objectAnimator?.reverse()
            return
        }

        objectAnimator?.end()

        val duration = if (immediate) 0L else duration
        val scaleXPropHolder = PropertyValuesHolder.ofFloat("scaleX", 4f, 1f)
        val scaleYPropHolder = PropertyValuesHolder.ofFloat("scaleY", 4f, 1f)
        val bgPropHolder = PropertyValuesHolder.ofObject(BackgroundColorProperty(),
            ArgbEvaluator(), endColor, startColor)

        objectAnimator = ObjectAnimator
            .ofPropertyValuesHolder(this,
                scaleXPropHolder, scaleYPropHolder, bgPropHolder
            )
            .setDuration(duration)

        objectAnimator?.start()
    }
}