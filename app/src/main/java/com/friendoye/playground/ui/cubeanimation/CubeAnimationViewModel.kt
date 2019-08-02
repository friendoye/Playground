package com.friendoye.playground.ui.cubeanimation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CubeAnimationViewModel : ViewModel() {
    private val _isExpanded by lazy {
        val data = MutableLiveData<Boolean>()
        data.value = false
        data
    }
    val isExpanded: LiveData<Boolean>
        get() = _isExpanded

    fun toggleExpandedStatus() {
        val newValue = _isExpanded.value!!.not()
        _isExpanded.value = newValue
    }
}
