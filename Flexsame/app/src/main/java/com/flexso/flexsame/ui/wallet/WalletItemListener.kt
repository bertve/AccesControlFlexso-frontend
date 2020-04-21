package com.flexso.flexsame.ui.wallet

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.flexso.flexsame.R
import com.flexso.flexsame.models.Office

class WalletItemListener(val clickListener: (office : Office) -> Unit) {
    fun onClick(v: View, o: Office) {
        clickListener(o)
        val key = v.findViewById<LottieAnimationView>(R.id.key_img)
        key.playAnimation()
    }
}