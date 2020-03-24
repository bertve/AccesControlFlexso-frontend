package com.flexso.flexsame.ui.wallet

import com.flexso.flexsame.models.Office

class WalletItemListener(val clickListener: (itemId : Long) -> Unit) {
    fun onClick(o : Office) = clickListener(o.officeId)
}