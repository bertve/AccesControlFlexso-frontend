package com.example.flexsame.ui.wallet

import com.example.flexsame.models.Office

class WalletItemListener(val clickListener: (itemId : Long) -> Unit) {
    fun onClick(o : Office) = clickListener(o.officeId)
}