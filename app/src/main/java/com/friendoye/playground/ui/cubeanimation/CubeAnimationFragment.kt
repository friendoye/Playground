package com.friendoye.playground.ui.cubeanimation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.friendoye.playground.R
import com.friendoye.playground.ui.cubeanimation.animators.*
import kotlinx.android.synthetic.main.cube_animation_fragment.*
import kotlin.properties.Delegates

class CubeAnimationFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = CubeAnimationFragment()
    }

    private lateinit var viewModel: CubeAnimationViewModel

    private val cubeAnimatorContext: CubeAnimator by lazy {
        SpringCubeAnimator(context!!)
        //CubeXmlAnimator(context!!)
        //ValueCubeAnimator(context!!)
        //CubeObjectAnimator(context!!)
        //`ObjectCubeAnimatorPlusPlus`(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.cube_animation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CubeAnimationViewModel::class.java)

        continuousAnimationTestSetup()
        //reentrantAndContinuousTestSetup()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cubeView.setOnClickListener { viewModel.toggleExpandedStatus() }
    }

    private fun continuousAnimationTestSetup() {
        viewModel.isExpanded.observe(this, object : InitialObserver<Boolean>() {
            override fun onUpdate(isExpandedValue: Boolean, isInitial: Boolean) {
                cubeAnimatorContext.apply {
                    if (isExpandedValue) {
                        cubeView.startExpandAnimation(immediate = isInitial)
                    } else {
                        cubeView.startCollapseAnimation(immediate = isInitial)
                    }
                }
            }
        })
    }

    private fun reentrantAndContinuousTestSetup() {
        viewModel.isExpanded.observe(this, object : InitialObserver<Boolean>() {
            override fun onUpdate(isExpandedValue: Boolean, isInitial: Boolean) {
                cubeAnimatorContext.apply {
                    if (isExpandedValue) {
                        cubeView.startExpandAnimation(immediate = isInitial)
                        cubeView.postDelayed({ cubeView.startExpandAnimation(immediate = isInitial) }, 500L)
                        cubeView.postDelayed({ cubeView.startExpandAnimation(immediate = isInitial) }, 1000L)
                        //cubeView.postDelayed({ cubeView.startExpandAnimation(immediate = isInitial) }, 4000L)
                    } else {
                        cubeView.startCollapseAnimation(immediate = isInitial)
                        cubeView.postDelayed({ cubeView.startCollapseAnimation(immediate = isInitial) }, 500L)
                        cubeView.postDelayed({ cubeView.startCollapseAnimation(immediate = isInitial) }, 1000L)
                        //cubeView.postDelayed({ cubeView.startCollapseAnimation(immediate = isInitial) }, 4000L)
                    }
                }
            }
        })
    }
}