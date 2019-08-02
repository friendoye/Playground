package com.friendoye.playground.ui.cubeanimation

import androidx.lifecycle.Observer

open class InitialObserver<T> : Observer<T> {

    private var isInitial: Boolean = true

    override fun onChanged(t: T) {
        onUpdate(t, isInitial)
        isInitial = false
    }

    open fun onUpdate(t: T, isInitial: Boolean) {

    }
}