package com.flexso.flexsame.utils

import android.animation.Animator
import com.airbnb.lottie.LottieAnimationView

class BackAndForthAnimatorListener(val anim: LottieAnimationView) : Animator.AnimatorListener {
    var counter = 0
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {

    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        anim.reverseAnimationSpeed()
        if (counter % 2 == 1) {
            anim.playAnimation()
        }
    }

    override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
        counter++
    }

    override fun onAnimationStart(animation: Animator?) {

    }
}