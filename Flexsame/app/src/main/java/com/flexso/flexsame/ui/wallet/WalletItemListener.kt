package com.flexso.flexsame.ui.wallet

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.WalletItemBinding
import com.flexso.flexsame.models.Office
import kotlinx.android.synthetic.main.wallet_item.view.*

class WalletItemListener(val clickListener: (itemId : Long) -> Unit) {
    fun onClick(v : View , o : Office) {
        clickListener(o.officeId)
        val key = v.findViewById<LottieAnimationView>(R.id.key_img)
        key.playAnimation()
    }
}