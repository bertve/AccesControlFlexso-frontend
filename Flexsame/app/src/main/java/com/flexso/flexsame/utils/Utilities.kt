package com.flexso.flexsame.utils

object Utilities {
    fun capitalizeWords(s : String) : String{
        var res : String = ""
        s.split(" ").map {
            res += it.capitalize()  + " "
        }
        return res.trim()
    }
}