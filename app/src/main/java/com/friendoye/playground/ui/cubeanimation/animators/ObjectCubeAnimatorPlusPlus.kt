package com.friendoye.playground.ui.cubeanimation.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.friendoye.playground.R


class ObjectCubeAnimatorPlusPlus(private val context: Context) : CubeAnimator {
    private val startColor by lazy { ContextCompat.getColor(context, R.color.startColor) }
    private val endColor by lazy { ContextCompat.getColor(context, R.color.endColor) }
    private val duration = 3000L
    @ColorInt
    private var currentColor: Int? = null

    private var runningAnimatorsSet = AnimatorSet()

    override fun View.startExpandAnimation(immediate: Boolean) = startAnimation(immediate, false)
    override fun View.startCollapseAnimation(immediate: Boolean) = startAnimation(immediate, true)

    private fun View.startAnimation(immediate: Boolean, reverse: Boolean) {
        var isPrevAnimWasReversed = getTag(R.id.tag_is_reversed) as? Boolean
        if (runningAnimatorsSet.childAnimations.all { it.isRunning }
            && isPrevAnimWasReversed == reverse) {
            return
        }

        // TODO: Add check if we want start animation, when
        // view already has properties with end animators values.

        if (immediate) {
            runningAnimatorsSet.childAnimations.forEach { it.end() }
        }

        if (runningAnimatorsSet.childAnimations.any { !it.isRunning }) {
            runningAnimatorsSet.childAnimations.forEach { it.end() }
            runningAnimatorsSet = AnimatorSet()
        }

        val duration = if (immediate) 0L else duration
        val animContext = ViewAnimatorContext(this, duration, reverse)
        if (runningAnimatorsSet.childAnimations.isEmpty()) {
            isPrevAnimWasReversed = null
            runningAnimatorsSet = AnimatorSet()
            animContext.setupAnimatorSet(runningAnimatorsSet)
        }

        setTag(R.id.tag_is_reversed, reverse)
        runningAnimatorsSet.childAnimations.asSequence()
            .map { it as ObjectAnimator }
            .forEach {
                val prevReversedState = isPrevAnimWasReversed ?: false
                if (prevReversedState != reverse) it.reverse() else it.start()
            }

        if (immediate) {
            runningAnimatorsSet = AnimatorSet()
        }
    }

    private fun ViewAnimatorContext.setupAnimatorSet(animatorSet: AnimatorSet) {
        animatorSet.apply {
            playTogether(
                createAnimatorScaleX(),
                createAnimatorScaleY(),
                createAnimatorBackgroundColor()
            )
        }
    }

    private fun ViewAnimatorContext.createAnimatorScaleX() =
        ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 4.0f)
            .setDuration(duration)

    private fun ViewAnimatorContext.createAnimatorScaleY() =
        ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 4.0f)
            .setDuration(duration)

    private fun ViewAnimatorContext.createAnimatorBackgroundColor() =
        ObjectAnimator.ofArgb(
            view, "backgroundColor",
            startColor,
            endColor
        )
            .setDuration(duration)
            .apply {
                addUpdateListener { animator ->
                    currentColor = animator.animatedValue as Int
                    Log.i("ContinuousCubeAnim", "Value: ${animator.animatedFraction}")
                }
            }

    data class ViewAnimatorContext(
        val view: View, val duration: Long, val reverse: Boolean
    )
}