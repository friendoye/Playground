package com.friendoye.playground.ui.cubeanimation.animators

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.friendoye.playground.R


class ValueCubeAnimator(private val context: Context) :
    CubeAnimator {
    private val startColor by lazy { ContextCompat.getColor(context, R.color.startColor) }
    private val endColor by lazy { ContextCompat.getColor(context, R.color.endColor) }
    private val argbEvaluator by lazy { ArgbEvaluator() }
    private val duration = 3000L

    override fun View.startExpandAnimation(immediate: Boolean) {
        val duration = if (immediate) 0L else duration
        val currentColor = backgroundColor ?: startColor
        val evaluator: (Float) -> Int = { fraction ->
            argbEvaluator.evaluate(fraction, currentColor, endColor) as Int
        }
        this.animate()
            .scaleX(4.0f)
            .scaleY(4.0f)
            .setUpdateListener { animator ->
                val fraction = animator.animatedFraction
                val color = evaluator.invoke(fraction)
                Log.i("NotContinuousCubeAnim", "Value: ${animator.animatedFraction}")
                this@startExpandAnimation.setBackgroundColor(color)
            }
            .setDuration(duration)
            .start()
    }

    override fun View.startCollapseAnimation(immediate: Boolean) {
        val duration = if (immediate) 0L else duration
        val currentColor = backgroundColor ?: endColor
        val evaluator: (Float) -> Int = { fraction ->
            argbEvaluator.evaluate(fraction, currentColor, startColor) as Int
        }
        this.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setUpdateListener { animator ->
                val fraction = animator.animatedFraction
                val color = evaluator.invoke(fraction)
                Log.i("NotContinuousCubeAnim", "Value: ${animator.animatedFraction}")
                this@startCollapseAnimation.setBackgroundColor(color)
            }
            .setDuration(duration)
            .start()
    }
}