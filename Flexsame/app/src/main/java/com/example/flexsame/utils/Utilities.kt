package com.example.flexsame.utils

import android.util.Log

object Utilities {
    fun capitalizeWords(s : String) : String{
        var res : String = ""
        s.split(" ").map {
            res += it.capitalize()  + " "
        }
        return res.trim()
    }
}