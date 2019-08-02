package com.friendoye.playground.ui.cubeanimation.animators

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.friendoye.playground.R

@SuppressLint("LongLogTag")
class CubeXmlAnimator(private val context: Context) :
    CubeAnimator {
    private val startColor by lazy { ContextCompat.getColor(context, R.color.startColor) }
    private val endColor by lazy { ContextCompat.getColor(context, R.color.endColor) }

    private var currentAnimator: AnimatorSet? = null

    override fun View.startExpandAnimation(immediate: Boolean) {
        currentAnimator?.end()
        currentAnimator = AnimatorInflater
            .loadAnimator(context, R.animator.cube_animator) as AnimatorSet
        currentAnimator?.let { currentAnimator ->
            currentAnimator.setTarget(this)
            val argbEvaluator = ArgbEvaluator()
            val evaluator: (Float) -> Int = { fraction ->
                argbEvaluator.evaluate(fraction, startColor, endColor) as Int
            }
            currentAnimator.setupBackgroundColorAnimator { animator ->
                val fraction = animator.animatedFraction
                val newColor = evaluator.invoke(fraction)
                this.setBackgroundColor(newColor)
                Log.i("CubeXmlAnimator", "Value: ${animator.animatedFraction}")
            }
            if (immediate) currentAnimator.end() else currentAnimator.start()
        }
    }

    override fun View.startCollapseAnimation(immediate: Boolean) {
        currentAnimator?.end()
        currentAnimator = AnimatorInflater
            .loadAnimator(context, R.animator.cube_animator_reverse) as AnimatorSet
        currentAnimator?.let { currentAnimator ->
            currentAnimator.setTarget(this)
            val argbEvaluator = ArgbEvaluator()
            val evaluator: (Float) -> Int = { fraction ->
                argbEvaluator.evaluate(fraction, endColor, startColor) as Int
            }
            currentAnimator.setupBackgroundColorAnimator { animator ->
                val fraction = animator.animatedFraction
                val newColor = evaluator.invoke(fraction)
                this.setBackgroundColor(newColor)
                Log.i("CubeXmlAnimator", "Value: ${animator.animatedFraction}")
            }
            if (immediate) currentAnimator.end() else currentAnimator.start()
        }
    }

    private fun AnimatorSet.setupBackgroundColorAnimator(onUpdate: (ObjectAnimator) -> Unit) {
        childAnimations
            .asSequence()
            .filter { (it as? ObjectAnimator)?.propertyName == "backgroundColor" }
            .map { it as ObjectAnimator }
            .forEach { colorAnimator ->
                colorAnimator.removeAllUpdateListeners()
                colorAnimator.addUpdateListener { onUpdate(colorAnimator) }
            }
    }
}