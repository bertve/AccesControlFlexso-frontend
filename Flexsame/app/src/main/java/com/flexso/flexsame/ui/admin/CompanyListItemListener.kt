package com.flexso.flexsame.ui.admin

import android.view.View
import com.flexso.flexsame.R
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User

class CompanyListItemListener (val clickListener: (itemId : Long) -> Unit) {
    fun onClick(v : View, u : User) {
        clickListener(u.userId)
    }
}