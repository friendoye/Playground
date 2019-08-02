package com.friendoye.playground.ui.cubeanimation.animators

import android.animation.ArgbEvaluator
import android.animation.TimeAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.util.Log
import android.util.Property
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import androidx.annotation.ColorInt
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlin.collections.HashMap

fun View.spring(
    property: FloatPropertyCompat<View>,
    damping: Float = SpringForce.DAMPING_RATIO_NO_BOUNCY,
    stiffness: Float = 50f
): SpringAnimation {
    val key = property.key
    var sprintAnim = getTag(key) as? SpringAnimation?
    if (sprintAnim == null) {
        sprintAnim = SpringAnimation(this, property).apply {
            spring = SpringForce().apply {
                this.dampingRatio = damping
                this.stiffness = stiffness
            }
        }
        setTag(key, sprintAnim)
    }
    return sprintAnim
}

fun View.backgroundColorSpring(@ColorInt startColor: Int,
                               @ColorInt endColor: Int): SpringAnimation {
    return spring(createBackgroundColorProperty(startColor, endColor)).apply {
        // Under the hood SpringAnimation sets minimumVisibleChange == 1f,
        // if using not DynamicAnimation.ViewProperty.
        // Manually set minimumVisibleChange to 0.01f, so color could be changed
        // in between 0f and 1f.
        minimumVisibleChange = 0.01f
    }
}

@get:ColorInt
val View.backgroundColor: Int?
    get() = (background as? ColorDrawable)?.color

class BackgroundColorProperty : Property<View, Int>(Int::class.java, "backgroundColor") {
    override fun set(view: View, value: Int) {
        view.setBackgroundColor(value)
    }

    override fun get(view: View): Int {
        return (view.background as? ColorDrawable)?.color ?: Color.WHITE
    }
}

private fun createBackgroundColorProperty(@ColorInt startColor: Int,
                                          @ColorInt endColor: Int): FloatPropertyCompat<View> {
    return object : FloatPropertyCompat<View>("backgroundColor") {
        val argbEvaluator by lazy { ArgbEvaluator() }
        var currentFloat: Float = 0.0f
        override fun setValue(view: View, value: Float) {
            currentFloat = value
            Log.i("BackgroundColorProperty", "Value: $value")
            val newColor = argbEvaluator.evaluate(value, startColor, endColor) as Int
            view.setBackgroundColor(newColor)
        }

        override fun getValue(view: View): Float {
            return currentFloat
        }
    }
}

private val propertyNameToIntTagMap = HashMap<String, Int>(4)
private val <T> FloatPropertyCompat<T>.key: Int
    get() {
        val f = FloatPropertyCompat::class.java.getDeclaredField("mPropertyName")
        f.isAccessible = true
        val propertyName = f.get(this) as String
        val intTag = propertyNameToIntTagMap[propertyName]
        return if (intTag == null) {
            val currentCounter = propertyNameToIntTagMap.size
            val newValue = (1 shl 25) + currentCounter
            propertyNameToIntTagMap.put(propertyName, newValue)
            newValue
        } else {
            intTag
        }
    }